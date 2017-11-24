package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxy;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import javax.annotation.Nullable;

/**
 * A capability proxy.
 * @author rubensworks
 */
public class TileCapabilityProxy extends CyclopsTileEntity {

    // A flag that is set when this tile is checking for a target's capability, to avoid infinite loops.
    protected boolean handling = false;

    public EnumFacing getFacing() {
        return getWorld().getBlockState(getPos()).getValue(BlockCapabilityProxy.FACING);
    }

    public static BlockPos getTargetPos(BlockPos source, EnumFacing facing) {
        return source.offset(facing);
    }

    protected BlockPos getTargetPos(IBlockAccess worldIn, @Nullable Capability<?> capability, BlockPos source) {
        return getTargetPos(source, getFacing());
    }

    @Nullable
    protected <T> T getTarget(Capability<T> capability, EnumFacing facing) {
        return TileHelpers.getCapability(getWorld(), getTargetPos(getWorld(), capability, getPos()), getFacing().getOpposite(), capability);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (handling) {
            return false;
        }
        handling = true;
        boolean ret = getTarget(capability, facing) != null;
        handling = false;
        return ret;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (handling) {
            return null;
        }
        handling = true;
        T ret = getTarget(capability, facing);
        handling = false;
        return ret;
    }
}
