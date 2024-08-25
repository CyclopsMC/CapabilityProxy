package org.cyclops.capabilityproxy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
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
@Deprecated
public class RegistryEntriesNeoForge { // TODO: rm

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityCapabilityProxy>> TILE_ENTITY_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("capabilityproxy:capability_proxy"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityEntityCapabilityProxy>> TILE_ENTITY_ENTITY_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("capabilityproxy:entity_capability_proxy"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityItemCapabilityProxy>> TILE_ENTITY_ITEM_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("capabilityproxy:item_capability_proxy"));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityRangedCapabilityProxy>> TILE_ENTITY_RANGED_CAPABILITY_PROXY = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("capabilityproxy:ranged_capability_proxy"));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerItemCapabilityProxy>> CONTAINER_ITEM_CAPABILITY_PROXY = DeferredHolder.create(Registries.MENU, ResourceLocation.parse("capabilityproxy:item_capability_proxy"));

}
