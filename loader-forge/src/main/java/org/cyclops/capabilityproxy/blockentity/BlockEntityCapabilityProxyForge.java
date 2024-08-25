package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.capabilityproxy.CapabilityProxyForge;
import org.cyclops.capabilityproxy.RegistryEntries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A capability proxy.
 * @author rubensworks
 */
public class BlockEntityCapabilityProxyForge extends BlockEntityCapabilityProxyCommon {

    private final Map<Pair<BlockPos, Capability<?>>, LazyOptional<?>> cachedCapabilities = Maps.newHashMap();

    public BlockEntityCapabilityProxyForge(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_CAPABILITY_PROXY.value(), blockPos, blockState);
    }

    protected BlockEntityCapabilityProxyForge(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public static BlockPos getTargetPos(BlockPos source, Direction facing) {
        return source.relative(facing);
    }

    protected BlockPos getTargetPos(Level worldIn, @Nullable Capability<?> capability, BlockPos source) {
        return getTargetPos(source, getFacing());
    }

    protected <T> LazyOptional<T> getTarget(Capability<T> capability, BlockGetter world, BlockPos pos, Direction facing) {
        return getCapabilityCached(cachedCapabilities, capability, pos,
                () -> CapabilityProxyForge._instance.getModHelpers().getCapabilityHelpers().getCapability(world, pos, facing, capability));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (handling) {
            return LazyOptional.empty();
        }
        handling = true;
        LazyOptional<T> ret = getTarget(capability, getLevel(), getTargetPos(getLevel(), capability, getBlockPos()), getFacing().getOpposite());
        handling = false;
        return ret == null ? LazyOptional.empty() : ret;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<?> value : cachedCapabilities.values()) {
            value.invalidate();
        }
        cachedCapabilities.clear();
    }

    public static <T, C> LazyOptional<T> getCapabilityCached(Map<Pair<C, Capability<?>>, LazyOptional<?>> cachedCapabilities,
                                                             Capability<T> capability, C cacheParam, Supplier<LazyOptional<T>> capabilitySupplier) {
        // Check if capability is in cache
        Pair<C, Capability<?>> cacheKey = Pair.of(cacheParam, capability);
        LazyOptional<?> cachedCapability = cachedCapabilities.get(cacheKey);
        if (cachedCapability != null) {
            return (LazyOptional<T>) cachedCapability;
        }

        // Retrieve the actual capability
        LazyOptional<T> innerCapability = capabilitySupplier.get();
        if (!innerCapability.isPresent()) {
            return LazyOptional.empty();
        }

        // Wrap the capability, cache it, and add invalidation listener
        LazyOptional<T> outerCapability = innerCapability.lazyMap((a) -> a);
        cachedCapabilities.put(cacheKey, outerCapability);
        innerCapability.addListener((a) -> {
            outerCapability.invalidate();
            cachedCapabilities.remove(cacheKey);
        });

        return outerCapability;
    }
}
