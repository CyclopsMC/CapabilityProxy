package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * An entity capability proxy.
 * @author josephcsible
 */
public class BlockEntityEntityCapabilityProxyCommon extends CyclopsBlockEntityCommon {

    public BlockEntityEntityCapabilityProxyCommon(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_ENTITY_CAPABILITY_PROXY.value(), blockPos, blockState);
    }

    public Direction getFacing() {
        return IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxy.FACING, Direction.UP);
    }
}
