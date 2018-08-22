package org.cyclops.capabilityproxy.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxy;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.tileentity.InventoryTileEntity;

import java.util.Collections;

/**
 * An item capability proxy.
 * @author rubensworks
 */
public class TileItemCapabilityProxy extends InventoryTileEntity {

    public TileItemCapabilityProxy() {
        super(1, "main", 1);
        for (EnumFacing side : EnumFacing.VALUES) {
            addSlotsToSide(side, Collections.singleton(0));
        }
    }

    public EnumFacing getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), BlockItemCapabilityProxy.FACING, EnumFacing.UP);
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        super.setInventorySlotContents(slotId, itemstack);
        boolean isEmpty = getStackInSlot(slotId).isEmpty();
        getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos())
             .withProperty(BlockItemCapabilityProxy.INACTIVE, isEmpty));
    }

    protected ItemStack getContents() {
        return getStackInSlot(0);
    }

    @Override
    protected boolean canAccess(int slot, EnumFacing side) {
        return side == getFacing();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return getContents().isEmpty();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            capability = CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
        }
        return facing == getFacing()
                ? super.hasCapability(capability, facing) : getContents().hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            capability = (Capability<T>) CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
        }
        return facing == getFacing()
                ? super.getCapability(capability, facing) : getContents().getCapability(capability, facing);
    }
}
