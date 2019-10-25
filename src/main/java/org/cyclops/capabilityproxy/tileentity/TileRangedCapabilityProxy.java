package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.cyclopscore.helper.TileHelpers;

import javax.annotation.Nullable;

/**
 * A ranged capability proxy.
 * @author rubensworks
 */
public class TileRangedCapabilityProxy extends TileCapabilityProxy {

    public TileRangedCapabilityProxy() {
        super(RegistryEntries.TILE_ENTITY_RANGED_CAPABILITY_PROXY);
    }

    @Nullable
    @Override
    protected <T> LazyOptional<T> getTarget(Capability<T> capability, Direction facing) {
        for (int offset = 1; offset < BlockRangedCapabilityProxyConfig.range; offset++) {
            BlockPos current = getPos().offset(getFacing(), offset);
            LazyOptional<T> instance = TileHelpers.getCapability(getWorld(), current, getFacing().getOpposite(), capability);
            if (instance.isPresent()) {
                return instance;
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().grow(BlockRangedCapabilityProxyConfig.range);
    }
}
