package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;

/**
 * A ranged capability proxy.
 * @author rubensworks
 */
public class TileRangedCapabilityProxy extends TileCapabilityProxy {

    public TileRangedCapabilityProxy() {
        super(RegistryEntries.TILE_ENTITY_RANGED_CAPABILITY_PROXY);
    }

    @Override
    protected <T> LazyOptional<T> getTarget(Capability<T> capability, IBlockReader world, BlockPos pos, Direction facing) {
        for (int offset = 1; offset < BlockRangedCapabilityProxyConfig.range; offset++) {
            BlockPos current = getBlockPos().relative(getFacing(), offset);
            LazyOptional<T> instance = super.getTarget(capability, world, current, getFacing().getOpposite());
            if (instance.isPresent()) {
                return instance;
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(BlockRangedCapabilityProxyConfig.range);
    }
}
