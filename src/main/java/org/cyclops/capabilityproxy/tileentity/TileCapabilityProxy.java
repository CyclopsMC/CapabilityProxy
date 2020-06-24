package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxy;
import org.cyclops.cyclopscore.helper.BlockHelpers;
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

    public TileCapabilityProxy() {
        super(RegistryEntries.TILE_ENTITY_CAPABILITY_PROXY);
    }

    protected TileCapabilityProxy(TileEntityType<?> type) {
        super(type);
    }

    public Direction getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), BlockCapabilityProxy.FACING, Direction.UP);
    }

    public static BlockPos getTargetPos(BlockPos source, Direction facing) {
        return source.offset(facing);
    }

    protected BlockPos getTargetPos(World worldIn, @Nullable Capability<?> capability, BlockPos source) {
        return getTargetPos(source, getFacing());
    }

    protected <T> LazyOptional<T> getTarget(Capability<T> capability, IBlockReader world, BlockPos pos, Direction facing) {
        if (ModList.get().isLoaded("commoncapabilities")) {
            LazyOptional<T> lazyOptional = BlockCapabilityProvider.getCapability(world.getBlockState(pos), capability, world, pos, facing);
            if (lazyOptional != null) {
                return lazyOptional;
            }
        }
        return TileHelpers.getCapability(world, pos, facing, capability);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (handling) {
            return LazyOptional.empty();
        }
        handling = true;
        LazyOptional<T> ret = getTarget(capability, getWorld(), getTargetPos(getWorld(), capability, getPos()), getFacing().getOpposite());
        handling = false;
        return ret == null ? LazyOptional.empty() : ret;
    }
}
