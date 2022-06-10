package org.cyclops.capabilityproxy;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxy;
import org.cyclops.capabilityproxy.blockentity.BlockEntityEntityCapabilityProxy;
import org.cyclops.capabilityproxy.blockentity.BlockEntityItemCapabilityProxy;
import org.cyclops.capabilityproxy.blockentity.BlockEntityRangedCapabilityProxy;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    @ObjectHolder(registryName = "item", value = "capabilityproxy:capability_proxy")
    public static final Item ITEM_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "item", value = "capabilityproxy:entity_capability_proxy")
    public static final Item ITEM_ENTITY_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "item", value = "capabilityproxy:item_capability_proxy")
    public static final Item ITEM_ITEM_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "item", value = "capabilityproxy:ranged_capability_proxy")
    public static final Item ITEM_RANGED_CAPABILITY_PROXY = null;

    @ObjectHolder(registryName = "block", value = "capabilityproxy:capability_proxy")
    public static final Block BLOCK_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "block", value = "capabilityproxy:entity_capability_proxy")
    public static final Block BLOCK_ENTITY_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "block", value = "capabilityproxy:item_capability_proxy")
    public static final Block BLOCK_ITEM_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "block", value = "capabilityproxy:ranged_capability_proxy")
    public static final Block BLOCK_RANGED_CAPABILITY_PROXY = null;

    @ObjectHolder(registryName = "block_entity_type", value = "capabilityproxy:capability_proxy")
    public static final BlockEntityType<BlockEntityCapabilityProxy> TILE_ENTITY_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "block_entity_type", value = "capabilityproxy:entity_capability_proxy")
    public static final BlockEntityType<BlockEntityEntityCapabilityProxy> TILE_ENTITY_ENTITY_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "block_entity_type", value = "capabilityproxy:item_capability_proxy")
    public static final BlockEntityType<BlockEntityItemCapabilityProxy> TILE_ENTITY_ITEM_CAPABILITY_PROXY = null;
    @ObjectHolder(registryName = "block_entity_type", value = "capabilityproxy:ranged_capability_proxy")
    public static final BlockEntityType<BlockEntityRangedCapabilityProxy> TILE_ENTITY_RANGED_CAPABILITY_PROXY = null;

    @ObjectHolder(registryName = "menu", value = "capabilityproxy:item_capability_proxy")
    public static final MenuType<ContainerItemCapabilityProxy> CONTAINER_ITEM_CAPABILITY_PROXY = null;

}
