package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityInvalidationListener;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxy;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.BlockHelpers;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A capability proxy.
 * @author rubensworks
 */
public class BlockEntityCapabilityProxy extends CyclopsBlockEntity {

    private final Map<Pair<BlockPos, BaseCapability<?, ?>>, Pair<?, ICapabilityInvalidationListener>> cachedCapabilities = Maps.newHashMap();

    // A flag that is set when this tile is checking for a target's capability, to avoid infinite loops.
    protected boolean handling = false;

    public BlockEntityCapabilityProxy(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_CAPABILITY_PROXY.get(), blockPos, blockState);
    }

    protected BlockEntityCapabilityProxy(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public Direction getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockCapabilityProxy.FACING, Direction.UP);
    }

    public static BlockPos getTargetPos(BlockPos source, Direction facing) {
        return source.relative(facing);
    }

    protected BlockPos getTargetPos(Level worldIn, BlockCapability<?, ?> capability, BlockPos source) {
        return getTargetPos(source, getFacing());
    }

    protected <T, C> T getTarget(BlockCapability<T, C> capability, Level targetWorld, BlockPos targetPos, C targetContext, Level originWorld, BlockPos originPos) {
        return getCapabilityCached(cachedCapabilities, capability, targetPos, targetWorld, targetPos, originWorld, originPos,
                () -> BlockEntityHelpers.getCapability(targetWorld, targetPos, targetContext, capability));
    }

    public <T, C> T getCapability(BlockCapability<T, C> capability, C context) {
        if (handling) {
            return null;
        }
        handling = true;
        T ret = getTarget(
                capability,
                getLevel(),
                getTargetPos(getLevel(), capability, getBlockPos()),
                context instanceof Direction ? (C) getFacing().getOpposite() : context,
                getLevel(),
                getBlockPos()
        );
        handling = false;
        return ret;
    }

    public static <T, C, CACHE> T getCapabilityCached(
            Map<Pair<CACHE, BaseCapability<?, ?>>, Pair<?, ICapabilityInvalidationListener>> cachedCapabilities,
            BaseCapability<T, C> capability,
            CACHE cacheParam,
            Level targetWorld,
            BlockPos targetPos,
            Level originWorld,
            BlockPos originPos,
            Supplier<Optional<T>> capabilitySupplier
    ) {
        // Check if capability is in cache
        Pair<CACHE, BaseCapability<?, ?>> cacheKey = Pair.of(cacheParam, capability);
        Pair<?, ICapabilityInvalidationListener> cachedCapabilityPair = cachedCapabilities.get(cacheKey);
        if (cachedCapabilityPair != null) {
            return (T) cachedCapabilityPair.getLeft();
        }

        // Retrieve the actual capability
        Optional<T> innerCapability = capabilitySupplier.get();
        if (!innerCapability.isPresent()) {
            return null;
        }

        T outerCapability = innerCapability.get();

        if (targetWorld instanceof ServerLevel targetWorldServer) {
            // Wrap the capability, cache it, and add invalidation listener
            ICapabilityInvalidationListener invalidationListener = () -> {
                cachedCapabilities.remove(cacheKey);
                originWorld.invalidateCapabilities(originPos);
                return false;
            };
            cachedCapabilities.put(cacheKey, Pair.of(outerCapability, invalidationListener));
            targetWorldServer.registerCapabilityListener(targetPos, invalidationListener);
        }

        return outerCapability;
    }
}
