package org.cyclops.capabilityproxy.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxy;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import java.util.List;
import java.util.Map;

/**
 * An entity capability proxy.
 * @author josephcsible
 */
public class TileEntityCapabilityProxy extends CyclopsTileEntity {

    private final Map<Pair<Integer, Capability<?>>, LazyOptional<?>> cachedCapabilities = Maps.newHashMap();

    public TileEntityCapabilityProxy() {
        super(RegistryEntries.TILE_ENTITY_ENTITY_CAPABILITY_PROXY);
    }

    public Direction getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockEntityCapabilityProxy.FACING, Direction.UP);
    }

    protected List<Entity> getEntities(Capability<?> capability) {
        AxisAlignedBB aabb = new AxisAlignedBB(getBlockPos().relative(getFacing()));
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
        return TileCapabilityProxy.getCapabilityCached(cachedCapabilities, capability, entity.getId(),
                () -> entity.getCapability(capability, getFacing().getOpposite()));
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<?> value : cachedCapabilities.values()) {
            value.invalidate();
        }
        cachedCapabilities.clear();
    }
}
