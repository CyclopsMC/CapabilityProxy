package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.commoncapabilities.api.capability.block.BlockCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * If common capabilities is loaded, this provider will be able to fetch block capabilities.
 * @author rubensworks
 */
public class BlockCapabilityProvider {

    public static <T> LazyOptional<T> getCapability(@Nonnull BlockState blockState, @Nonnull Capability<T> capability, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nullable Direction facing) {
        return BlockCapabilities.getInstance().getCapability(blockState, capability, world, pos, facing);
    }

}
