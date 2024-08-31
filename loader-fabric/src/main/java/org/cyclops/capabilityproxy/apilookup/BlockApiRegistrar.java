package org.cyclops.capabilityproxy.apilookup;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyFabric;

/**
 * A helper class to register delegating block apis for the given block entity
 * @author rubensworks
 */
public class BlockApiRegistrar {

    private boolean initialized = false;

    public void initializeCapabilityRegistrationsIfNeeded(BlockEntityType<? extends BlockEntityCapabilityProxyFabric> blockEntityType) {
        if (!initialized) {
            initialized = true;

            // The following force-loads the default Fabric capabilities
            FluidStorage.SIDED.apiClass();
            ItemStorage.SIDED.apiClass();

            for (BlockApiLookup<?, ?> capability : TypedApiHelpers.getTypedApiLookups((Class<BlockApiLookup<?, ?>>) (Class) BlockApiLookupImpl.class)) {
                registerCapability(blockEntityType, capability);
            }
        }
    }

    public <T, C> void registerCapability(BlockEntityType<? extends BlockEntityCapabilityProxyFabric> blockEntityType, BlockApiLookup<T, C> blockApiLookup) {
        blockApiLookup.registerForBlockEntity((blockEntity, context) -> blockEntity.getCapability(blockApiLookup, context), blockEntityType);
    }

}
