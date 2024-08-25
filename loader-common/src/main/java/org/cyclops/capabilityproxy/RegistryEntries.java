package org.cyclops.capabilityproxy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.DeferredHolderCommon;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolderCommon<Item, Item> ITEM_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("capabilityproxy:capability_proxy"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENTITY_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("capabilityproxy:entity_capability_proxy"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ITEM_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("capabilityproxy:item_capability_proxy"));
    public static final DeferredHolderCommon<Item, Item> ITEM_RANGED_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("capabilityproxy:ranged_capability_proxy"));

    public static final DeferredHolderCommon<Block, Block> BLOCK_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.BLOCK, ResourceLocation.parse("capabilityproxy:capability_proxy"));
    public static final DeferredHolderCommon<Block, Block> BLOCK_ENTITY_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.BLOCK, ResourceLocation.parse("capabilityproxy:entity_capability_proxy"));
    public static final DeferredHolderCommon<Block, Block> BLOCK_ITEM_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.BLOCK, ResourceLocation.parse("capabilityproxy:item_capability_proxy"));
    public static final DeferredHolderCommon<Block, Block> BLOCK_RANGED_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.BLOCK, ResourceLocation.parse("capabilityproxy:ranged_capability_proxy"));

    // TODO
//    public static final DeferredHolderCommon<BlockEntityType<?>, BlockEntityType<BlockEntityCapabilityProxy>> TILE_ENTITY_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("capabilityproxy:capability_proxy"));
//    public static final DeferredHolderCommon<BlockEntityType<?>, BlockEntityType<BlockEntityEntityCapabilityProxy>> TILE_ENTITY_ENTITY_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("capabilityproxy:entity_capability_proxy"));
//    public static final DeferredHolderCommon<BlockEntityType<?>, BlockEntityType<BlockEntityItemCapabilityProxy>> TILE_ENTITY_ITEM_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("capabilityproxy:item_capability_proxy"));
//    public static final DeferredHolderCommon<BlockEntityType<?>, BlockEntityType<BlockEntityRangedCapabilityProxy>> TILE_ENTITY_RANGED_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("capabilityproxy:ranged_capability_proxy"));
//
//    public static final DeferredHolderCommon<MenuType<?>, MenuType<ContainerItemCapabilityProxy>> CONTAINER_ITEM_CAPABILITY_PROXY = DeferredHolderCommon.create(Registries.MENU, ResourceLocation.parse("capabilityproxy:item_capability_proxy"));

}
