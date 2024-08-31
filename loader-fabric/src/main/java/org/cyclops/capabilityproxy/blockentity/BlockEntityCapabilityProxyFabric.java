package org.cyclops.capabilityproxy.blockentity;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.capabilityproxy.RegistryEntries;

/**
 * A capability proxy.
 * @author rubensworks
 */
public class BlockEntityCapabilityProxyFabric extends BlockEntityCapabilityProxyCommon {

    public BlockEntityCapabilityProxyFabric(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_CAPABILITY_PROXY.value(), blockPos, blockState);
        BlockEntityCapabilityProxyFabricConfig.blockApiRegistrar.initializeCapabilityRegistrationsIfNeeded((BlockEntityType<BlockEntityCapabilityProxyFabric>) getType());
    }

    protected BlockEntityCapabilityProxyFabric(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    protected BlockPos getTargetPos(Level worldIn, BlockApiLookup<?, ?> capability, BlockPos source) {
        return getTargetPos(source, getFacing());
    }

    protected <T, C> T getTarget(BlockApiLookup<T, C> capability, Level targetWorld, BlockPos targetPos, C targetContext, Level originWorld, BlockPos originPos) {
        return capability.find(targetWorld, targetPos, targetContext);
    }

    public <T, C> T getCapability(BlockApiLookup<T, C> capability, C context) {
        if (handling) {
            return null;
        }
        handling = true;
        T ret = getTarget(
                capability,
                getLevel(),
                getTargetPos(getLevel(), capability, getBlockPos()),
                context instanceof Direction ? (C) getFacing().getOpposite() : context,
                getLevel(),
                getBlockPos()
        );
        handling = false;
        return ret;
    }
}
