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
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.LootTables;
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
        protected void saveAdvancement(DirectoryCache cache, JsonObject json, Path path) {
            if (path.equals(ADV_ROOT)) return; //We NEVER care about this.
            super.saveAdvancement(cache, json, path);
        }

        @Override
        protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
            ShapedRecipeBuilder.shaped(RegistryEntries.ITEM_CAPABILITY_PROXY)
            .define('I', Tags.Items.INGOTS_IRON)
            .define('O', Tags.Items.OBSIDIAN)
            .define('N', Items.NETHER_WART)
            .pattern("IOI")
            .pattern("ONO")
            .pattern("IOI")
            .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(consumer);

            ShapedRecipeBuilder.shaped(RegistryEntries.ITEM_ENTITY_CAPABILITY_PROXY)
            .define('E', Items.ENDER_EYE)
            .define('C', Items.POPPED_CHORUS_FRUIT)
            .define('P', RegistryEntries.ITEM_CAPABILITY_PROXY)
            .pattern("ECE")
            .pattern("PPP")
            .pattern("ECE")
            .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
            .unlockedBy("has_popped_chorus_fruit", has(Items.POPPED_CHORUS_FRUIT))
            .unlockedBy("has_cap_proxy", has(RegistryEntries.ITEM_CAPABILITY_PROXY))
            .save(consumer);

            ShapelessRecipeBuilder.shapeless(RegistryEntries.ITEM_ITEM_CAPABILITY_PROXY)
            .requires(RegistryEntries.ITEM_CAPABILITY_PROXY)
            .requires(Tags.Items.CHESTS)
            .requires(Tags.Items.INGOTS_GOLD)
            .unlockedBy("has_cap_proxy", has(RegistryEntries.ITEM_CAPABILITY_PROXY))
            .unlockedBy("has_chest", has(Items.CHEST))
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(consumer);

            ShapedRecipeBuilder.shaped(RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY)
            .define('E', Tags.Items.ENDER_PEARLS)
            .define('P', RegistryEntries.ITEM_CAPABILITY_PROXY)
            .pattern(" E ")
            .pattern("PPP")
            .pattern(" E ")
            .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
            .unlockedBy("has_cap_proxy", has(RegistryEntries.ITEM_CAPABILITY_PROXY))
            .save(consumer);
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
        public void run(DirectoryCache cache) {
            Map<ResourceLocation, LootTable> map = new HashMap<>();
            ThreeConsumer<LootParameterSet, ResourceLocation, LootTable.Builder> consumer = (set, key, builder) -> {
                if (map.put(key, builder.setParamSet(set).build()) != null)
                    throw new IllegalStateException("Duplicate loot table " + key);
            };

            new Blocks().accept((key, builder) -> consumer.accept(LootParameterSets.BLOCK, key, builder));

            map.forEach((key, table) -> {
                Path target = this.gen.getOutputFolder().resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");

                try {
                   IDataProvider.save(GSON, cache, LootTableManager.serialize(table), target);
                } catch (IOException ioexception) {
                   LOGGER.error("Couldn't save loot table {}", target, ioexception);
                }
            });
        }

        private class Blocks extends BlockLootTables {
            private Set<Block> knownBlocks = new HashSet<>();

            protected void addTables() {
                this.dropSelf(RegistryEntries.BLOCK_CAPABILITY_PROXY);
                this.dropSelf(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY);
                this.dropSelf(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY);
                this.dropSelf(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY);
            }

            @Override
            public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
                this.addTables();

                Set<ResourceLocation> visited = new HashSet<>();

                for(Block block : knownBlocks) {
                   ResourceLocation tabke_name = block.getLootTable();
                   if (tabke_name != LootTables.EMPTY && visited.add(tabke_name)) {
                      LootTable.Builder builder = this.map.remove(tabke_name);
                      if (builder == null)
                         throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", tabke_name, block.getRegistryName()));

                      consumer.accept(tabke_name, builder);
                   }
                }

                if (!this.map.isEmpty())
                   throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
            }

            @Override
            public void dropSelf(Block block) {
                knownBlocks.add(block);
                super.dropSelf(block);
            }
        }
    }
}
