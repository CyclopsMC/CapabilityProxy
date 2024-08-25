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
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A capability proxy.
 * @author rubensworks
 */
public class BlockEntityCapabilityProxyNeoForge extends BlockEntityCapabilityProxyCommon {

    private final Map<Pair<BlockPos, BaseCapability<?, ?>>, Pair<?, ICapabilityInvalidationListener>> cachedCapabilities = Maps.newHashMap();

    public BlockEntityCapabilityProxyNeoForge(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_CAPABILITY_PROXY.value(), blockPos, blockState);
    }

    protected BlockEntityCapabilityProxyNeoForge(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
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
