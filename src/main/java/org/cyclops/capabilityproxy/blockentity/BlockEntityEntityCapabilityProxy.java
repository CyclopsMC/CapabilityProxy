package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;

import java.util.List;
import java.util.Map;

/**
 * An entity capability proxy.
 * @author josephcsible
 */
public class BlockEntityEntityCapabilityProxy extends CyclopsBlockEntity {

    private final Map<Pair<Integer, Capability<?>>, LazyOptional<?>> cachedCapabilities = Maps.newHashMap();

    public BlockEntityEntityCapabilityProxy(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_ENTITY_CAPABILITY_PROXY, blockPos, blockState);
    }

    public Direction getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxy.FACING, Direction.UP);
    }

    protected List<Entity> getEntities(Capability<?> capability) {
        AABB aabb = new AABB(getBlockPos().relative(getFacing()));
        Direction facing = getFacing().getOpposite();
        return getLevel().getEntitiesOfClass(Entity.class, aabb, entity -> entity.getCapability(capability, facing).isPresent());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        List<Entity> entities = getEntities(capability);
        if (entities.isEmpty()) {
            return LazyOptional.empty();
        }
        Entity entity = entities.get(0);
        return BlockEntityCapabilityProxy.getCapabilityCached(cachedCapabilities, capability, entity.getId(),
                () -> entity.getCapability(capability, getFacing().getOpposite()));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<?> value : cachedCapabilities.values()) {
            value.invalidate();
        }
        cachedCapabilities.clear();
    }
}
