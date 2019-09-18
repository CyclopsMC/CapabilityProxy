package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxy;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import java.util.List;

/**
 * An entity capability proxy.
 * @author josephcsible
 */
public class TileEntityCapabilityProxy extends CyclopsTileEntity {

    public TileEntityCapabilityProxy() {
        super(RegistryEntries.TILE_ENTITY_ENTITY_CAPABILITY_PROXY);
    }

    public Direction getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), BlockEntityCapabilityProxy.FACING, Direction.UP);
    }

    protected List<Entity> getEntities(Capability<?> capability) {
        AxisAlignedBB aabb = new AxisAlignedBB(getPos().offset(getFacing()));
        Direction facing = getFacing().getOpposite();
        return getWorld().getEntitiesWithinAABB(Entity.class, aabb, entity -> entity.getCapability(capability, facing).isPresent());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        List<Entity> entities = getEntities(capability);
        return entities.isEmpty() ? LazyOptional.empty() : entities.get(0).getCapability(capability, getFacing().getOpposite());
    }
}
