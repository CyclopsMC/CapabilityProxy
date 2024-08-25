package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxy;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * A capability proxy.
 * @author rubensworks
 */
public class BlockEntityCapabilityProxyCommon extends CyclopsBlockEntityCommon {

    // A flag that is set when this tile is checking for a target's capability, to avoid infinite loops.
    protected boolean handling = false;

    public BlockEntityCapabilityProxyCommon(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_CAPABILITY_PROXY.value(), blockPos, blockState);
    }

    protected BlockEntityCapabilityProxyCommon(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public Direction getFacing() {
        return IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockCapabilityProxy.FACING, Direction.UP);
    }

    public static BlockPos getTargetPos(BlockPos source, Direction facing) {
        return source.relative(facing);
    }
}
