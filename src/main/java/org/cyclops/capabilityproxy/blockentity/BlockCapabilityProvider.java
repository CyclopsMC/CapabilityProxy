package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
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

    public static <T> LazyOptional<T> getCapability(@Nonnull BlockState blockState, @Nonnull Capability<T> capability, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nullable Direction facing) {
        return BlockCapabilities.getInstance().getCapability(blockState, capability, world, pos, facing);
    }

}
