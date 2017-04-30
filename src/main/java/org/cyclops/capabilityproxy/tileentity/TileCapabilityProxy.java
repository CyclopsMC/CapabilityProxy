package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxy;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

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

    protected TileEntity getTarget() {
        return TileHelpers.getSafeTile(getWorld(), getPos().offset(getFacing()), TileEntity.class);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (handling) {
            return false;
        }
        TileEntity target = getTarget();
        handling = true;
        boolean ret = target != null && target.hasCapability(capability, getFacing().getOpposite());
        handling = false;
        return ret;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (handling) {
            return null;
        }
        TileEntity target = getTarget();
        handling = true;
        T ret = target != null ? target.getCapability(capability, getFacing().getOpposite()) : null;
        handling = false;
        return ret;
    }
}
