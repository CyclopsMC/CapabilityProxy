package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;

/**
 * A ranged capability proxy.
 * @author rubensworks
 */
public class BlockEntityRangedCapabilityProxy extends BlockEntityCapabilityProxy {

    public BlockEntityRangedCapabilityProxy(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_RANGED_CAPABILITY_PROXY, blockPos, blockState);
    }

    @Override
    protected <T> LazyOptional<T> getTarget(Capability<T> capability, BlockGetter world, BlockPos pos, Direction facing) {
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
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(BlockRangedCapabilityProxyConfig.range);
    }
}
