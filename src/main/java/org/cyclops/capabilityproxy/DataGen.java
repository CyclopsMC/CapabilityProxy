package org.cyclops.capabilityproxy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.util.ThreeConsumer;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = Bus.MOD)
public class DataGen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if (event.includeServer()) {
            gen.addProvider(new Recipes(gen));
            gen.addProvider(new Loots(gen));
        }
    }

    private static class Recipes extends RecipeProvider {
        private final DataGenerator gen;
        private final Path ADV_ROOT;

        public Recipes(DataGenerator gen) {
            super(gen);
            this.gen = gen;
            ADV_ROOT = this.gen.getOutputFolder().resolve("data/minecraft/advancements/recipes/root.json");
        }

        @Override
        protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject json, Path path) {
            if (path.equals(ADV_ROOT)) return; //We NEVER care about this.
            super.saveRecipeAdvancement(cache, json, path);
        }

        @Override
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            ShapedRecipeBuilder.shapedRecipe(RegistryEntries.ITEM_CAPABILITY_PROXY)
            .key('I', Tags.Items.INGOTS_IRON)
            .key('O', Tags.Items.OBSIDIAN)
            .key('N', Items.NETHER_WART)
            .patternLine("IOI")
            .patternLine("ONO")
            .patternLine("IOI")
            .addCriterion("has_obsidian", hasItem(Items.OBSIDIAN))
            .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
            .build(consumer);

            ShapedRecipeBuilder.shapedRecipe(RegistryEntries.ITEM_ENTITY_CAPABILITY_PROXY)
            .key('E', Items.ENDER_EYE)
            .key('C', Items.POPPED_CHORUS_FRUIT)
            .key('P', RegistryEntries.ITEM_CAPABILITY_PROXY)
            .patternLine("ECE")
            .patternLine("PPP")
            .patternLine("ECE")
            .addCriterion("has_ender_eye", hasItem(Items.ENDER_EYE))
            .addCriterion("has_popped_chorus_fruit", hasItem(Items.POPPED_CHORUS_FRUIT))
            .addCriterion("has_cap_proxy", hasItem(RegistryEntries.ITEM_CAPABILITY_PROXY))
            .build(consumer);

            ShapelessRecipeBuilder.shapelessRecipe(RegistryEntries.ITEM_ITEM_CAPABILITY_PROXY)
            .addIngredient(RegistryEntries.ITEM_CAPABILITY_PROXY)
            .addIngredient(Tags.Items.CHESTS)
            .addIngredient(Tags.Items.INGOTS_GOLD)
            .addCriterion("has_cap_proxy", hasItem(RegistryEntries.ITEM_CAPABILITY_PROXY))
            .addCriterion("has_chest", hasItem(Items.CHEST))
            .addCriterion("has_gold_ingot", hasItem(Items.GOLD_INGOT))
            .build(consumer);

            ShapedRecipeBuilder.shapedRecipe(RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY)
            .key('E', Tags.Items.ENDER_PEARLS)
            .key('P', RegistryEntries.ITEM_CAPABILITY_PROXY)
            .patternLine(" E ")
            .patternLine("PPP")
            .patternLine(" E ")
            .addCriterion("has_ender_pearl", hasItem(Items.ENDER_PEARL))
            .addCriterion("has_cap_proxy", hasItem(RegistryEntries.ITEM_CAPABILITY_PROXY))
            .build(consumer);
        }
    }

    private static class Loots implements IDataProvider {
        private final DataGenerator gen;

        public Loots(DataGenerator gen) {
            this.gen = gen;
        }

        @Override
        public String getName() {
            return "LootTables";
        }

        @Override
        public void act(DirectoryCache cache) {
            Map<ResourceLocation, LootTable> map = new HashMap<>();
            ThreeConsumer<LootParameterSet, ResourceLocation, LootTable.Builder> consumer = (set, key, builder) -> {
                if (map.put(key, builder.setParameterSet(set).build()) != null)
                    throw new IllegalStateException("Duplicate loot table " + key);
            };

            new Blocks().accept((key, builder) -> consumer.accept(LootParameterSets.BLOCK, key, builder));

            map.forEach((key, table) -> {
                Path target = this.gen.getOutputFolder().resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");

                try {
                   IDataProvider.save(GSON, cache, LootTableManager.toJson(table), target);
                } catch (IOException ioexception) {
                   LOGGER.error("Couldn't save loot table {}", target, ioexception);
                }
            });
        }

        private class Blocks extends BlockLootTables {
            private Set<Block> knownBlocks = new HashSet<>();

            protected void addTables() {
                this.registerDropSelfLootTable(RegistryEntries.BLOCK_CAPABILITY_PROXY);
                this.registerDropSelfLootTable(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY);
                this.registerDropSelfLootTable(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY);
                this.registerDropSelfLootTable(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY);
            }

            @Override
            public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
                this.addTables();

                Set<ResourceLocation> visited = new HashSet<>();

                for(Block block : knownBlocks) {
                   ResourceLocation tabke_name = block.getLootTable();
                   if (tabke_name != LootTables.EMPTY && visited.add(tabke_name)) {
                      LootTable.Builder builder = this.lootTables.remove(tabke_name);
                      if (builder == null)
                         throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", tabke_name, block.getRegistryName()));

                      consumer.accept(tabke_name, builder);
                   }
                }

                if (!this.lootTables.isEmpty())
                   throw new IllegalStateException("Created block loot tables for non-blocks: " + this.lootTables.keySet());
            }

            @Override
            public void registerDropSelfLootTable(Block block) {
                knownBlocks.add(block);
                super.registerDropSelfLootTable(block);
            }
        }
    }
}
