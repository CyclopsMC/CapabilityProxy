package org.cyclops.capabilityproxy;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileEntityCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileItemCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    @ObjectHolder("capabilityproxy:capability_proxy")
    public static final Item ITEM_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:entity_capability_proxy")
    public static final Item ITEM_ENTITY_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:item_capability_proxy")
    public static final Item ITEM_ITEM_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:ranged_capability_proxy")
    public static final Item ITEM_RANGED_CAPABILITY_PROXY = null;

    @ObjectHolder("capabilityproxy:capability_proxy")
    public static final Block BLOCK_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:entity_capability_proxy")
    public static final Block BLOCK_ENTITY_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:item_capability_proxy")
    public static final Block BLOCK_ITEM_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:ranged_capability_proxy")
    public static final Block BLOCK_RANGED_CAPABILITY_PROXY = null;

    @ObjectHolder("capabilityproxy:capability_proxy")
    public static final TileEntityType<TileCapabilityProxy> TILE_ENTITY_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:entity_capability_proxy")
    public static final TileEntityType<TileEntityCapabilityProxy> TILE_ENTITY_ENTITY_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:item_capability_proxy")
    public static final TileEntityType<TileItemCapabilityProxy> TILE_ENTITY_ITEM_CAPABILITY_PROXY = null;
    @ObjectHolder("capabilityproxy:ranged_capability_proxy")
    public static final TileEntityType<TileRangedCapabilityProxy> TILE_ENTITY_RANGED_CAPABILITY_PROXY = null;

    @ObjectHolder("capabilityproxy:item_capability_proxy")
    public static final ContainerType<ContainerItemCapabilityProxy> CONTAINER_ITEM_CAPABILITY_PROXY = null;

}
