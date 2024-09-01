package org.cyclops.capabilityproxy.apilookup;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.fabricmc.fabric.impl.lookup.item.ItemApiLookupImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.blockentity.BlockEntityItemCapabilityProxyFabric;

import java.util.Map;

/**
 * A helper class to register delegating item apis for the given block entity
 * @author rubensworks
 */
public class ItemApiRegistrar {

    private boolean initialized = false;

    public void initializeCapabilityRegistrationsIfNeeded(BlockEntityType<? extends BlockEntityItemCapabilityProxyFabric> blockEntityType) {
        if (!initialized) {
            initialized = true;

            // The following force-loads the default Fabric capabilities
            FluidStorage.SIDED.apiClass();
            ItemStorage.SIDED.apiClass();

            Map<ResourceLocation, ItemApiLookup<?, ?>> itemCapabilities = TypedApiHelpers.getTypedApiLookupsKeyed((Class<ItemApiLookup<?, ?>>) (Class) ItemApiLookupImpl.class);
            Map<ResourceLocation, BlockApiLookup<?, ?>> blockCapabilities = TypedApiHelpers.getTypedApiLookupsKeyed((Class<BlockApiLookup<?, ?>>) (Class) BlockApiLookupImpl.class);

            BlockEntityItemCapabilityProxyFabric.BLOCK_TO_ITEM_CAPABILITIES = Maps.newIdentityHashMap();
            for (Map.Entry<ResourceLocation, BlockApiLookup<?, ?>> entry : blockCapabilities.entrySet()) {
                registerCapability(blockEntityType, entry.getValue());

                // Heuristically try to match block caps with entity caps
                ItemApiLookup<?, ?> itemCapability = itemCapabilities.get(entry.getKey());
                if (itemCapability != null) {
                    BlockEntityItemCapabilityProxyFabric.BLOCK_TO_ITEM_CAPABILITIES.put(entry.getValue(), itemCapability);
                }
            }
        }
    }

    public <T, C> void registerCapability(BlockEntityType<? extends BlockEntityItemCapabilityProxyFabric> blockEntityType, BlockApiLookup<T, C> blockApiLookup) {
        blockApiLookup.registerForBlockEntity((blockEntity, context) -> blockEntity.getCapability(blockApiLookup, context), blockEntityType);
    }

}
