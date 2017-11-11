package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.cyclopscore.helper.TileHelpers;

import javax.annotation.Nullable;

/**
 * A ranged capability proxy.
 * @author rubensworks
 */
public class TileRangedCapabilityProxy extends TileCapabilityProxy {

    @Nullable
    @Override
    protected <T> T getTarget(Capability<T> capability, EnumFacing facing) {
        for (int offset = 1; offset < BlockRangedCapabilityProxyConfig.range; offset++) {
            BlockPos current = getPos().offset(getFacing(), offset);
            T instance = TileHelpers.getCapability(getWorld(), current, getFacing().getOpposite(), capability);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().grow(BlockRangedCapabilityProxyConfig.range);
    }
}
