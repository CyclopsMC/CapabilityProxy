package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxy;
import org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxy;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import java.util.List;

/**
 * An entity capability proxy.
 * @author josephcsible
 */
public class TileEntityCapabilityProxy extends CyclopsTileEntity {

    public EnumFacing getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), BlockEntityCapabilityProxy.FACING, EnumFacing.UP);
    }

    protected List<Entity> getEntities(Capability<?> capability) {
        AxisAlignedBB aabb = new AxisAlignedBB(getPos().offset(getFacing()));
        EnumFacing facing = getFacing().getOpposite();
        return getWorld().getEntitiesWithinAABB(Entity.class, aabb, entity -> entity.hasCapability(capability, facing));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return !getEntities(capability).isEmpty();
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        List<Entity> entities = getEntities(capability);
        return entities.isEmpty() ? null : entities.get(0).getCapability(capability, getFacing().getOpposite());
    }
}
