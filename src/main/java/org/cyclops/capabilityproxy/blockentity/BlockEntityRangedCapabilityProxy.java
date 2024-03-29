package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;

/**
 * A ranged capability proxy.
 * @author rubensworks
 */
public class BlockEntityRangedCapabilityProxy extends BlockEntityCapabilityProxy {

    public BlockEntityRangedCapabilityProxy(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_RANGED_CAPABILITY_PROXY.get(), blockPos, blockState);
    }

    @Override
    protected <T, C> T getTarget(BlockCapability<T, C> capability, ServerLevel targetWorld, BlockPos targetPos, C targetContext, ServerLevel originWorld, BlockPos originPos) {
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
