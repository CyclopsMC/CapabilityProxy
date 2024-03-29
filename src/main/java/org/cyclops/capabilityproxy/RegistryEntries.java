package org.cyclops.capabilityproxy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxy;
import org.cyclops.capabilityproxy.blockentity.BlockEntityEntityCapabilityProxy;
import org.cyclops.capabilityproxy.blockentity.BlockEntityItemCapabilityProxy;
import org.cyclops.capabilityproxy.blockentity.BlockEntityRangedCapabilityProxy;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolder<Item, Item> ITEM_CAPABILITY_PROXY = DeferredHolder.create(Registries.ITEM, new ResourceLocation("capabilityproxy:capability_proxy"));
    public static final DeferredHolder<Item, Item> ITEM_ENTITY_CAPABILITY_PROXY = DeferredHolder.create(Registries.ITEM, new ResourceLocation("capabilityproxy:entity_capability_proxy"));
    public static final DeferredHolder<Item, Item> ITEM_ITEM_CAPABILITY_PROXY = DeferredHolder.create(Registries.ITEM, new ResourceLocation("capabilityproxy:item_capability_proxy"));
    public static final DeferredHolder<Item, Item> ITEM_RANGED_CAPABILITY_PROXY = DeferredHolder.create(Registries.ITEM, new ResourceLocation("capabilityproxy:ranged_capability_proxy"));

    public static final DeferredHolder<Block, Block> BLOCK_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK, new ResourceLocation("capabilityproxy:capability_proxy"));
    public static final DeferredHolder<Block, Block> BLOCK_ENTITY_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK, new ResourceLocation("capabilityproxy:entity_capability_proxy"));
    public static final DeferredHolder<Block, Block> BLOCK_ITEM_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK, new ResourceLocation("capabilityproxy:item_capability_proxy"));
    public static final DeferredHolder<Block, Block> BLOCK_RANGED_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK, new ResourceLocation("capabilityproxy:ranged_capability_proxy"));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityCapabilityProxy>> TILE_ENTITY_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("capabilityproxy:capability_proxy"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityEntityCapabilityProxy>> TILE_ENTITY_ENTITY_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("capabilityproxy:entity_capability_proxy"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityItemCapabilityProxy>> TILE_ENTITY_ITEM_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("capabilityproxy:item_capability_proxy"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityRangedCapabilityProxy>> TILE_ENTITY_RANGED_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("capabilityproxy:ranged_capability_proxy"));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerItemCapabilityProxy>> CONTAINER_ITEM_CAPABILITY_PROXY = DeferredHolder.create(Registries.MENU, new ResourceLocation("capabilityproxy:item_capability_proxy"));

}
