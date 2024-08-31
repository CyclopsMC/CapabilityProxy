package org.cyclops.capabilityproxy.blockentity;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;

/**
 * A ranged capability proxy.
 * @author rubensworks
 */
public class BlockEntityRangedCapabilityProxyFabric extends BlockEntityCapabilityProxyFabric {

    public BlockEntityRangedCapabilityProxyFabric(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_RANGED_CAPABILITY_PROXY.value(), blockPos, blockState);
        BlockEntityRangedCapabilityProxyFabricConfig.blockApiRegistrar.initializeCapabilityRegistrationsIfNeeded((BlockEntityType<? extends BlockEntityCapabilityProxyFabric>) getType());
    }

    @Override
    protected <T, C> T getTarget(BlockApiLookup<T, C> capability, Level targetWorld, BlockPos targetPos, C targetContext, Level originWorld, BlockPos originPos) {
        for (int offset = 1; offset < BlockRangedCapabilityProxyConfig.range; offset++) {
            BlockPos current = getBlockPos().relative(getFacing(), offset);
            T instance = super.getTarget(
                    capability,
                    targetWorld,
                    current,
                    targetContext,
                    originWorld,
                    originPos
            );
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }
}
