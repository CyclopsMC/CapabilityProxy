package org.cyclops.capabilityproxy.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxy;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * An item capability proxy.
 * @author rubensworks
 */
public class TileItemCapabilityProxy extends CyclopsTileEntity implements INamedContainerProvider {

    private final SimpleInventory inventory;
    private final Map<Pair<String, Capability<?>>, LazyOptional<?>> cachedCapabilities = Maps.newHashMap();

    public TileItemCapabilityProxy() {
        super(RegistryEntries.TILE_ENTITY_ITEM_CAPABILITY_PROXY);
        this.inventory = new SimpleInventory(1, 1) {
            @Override
            public void setInventorySlotContents(int slotId, ItemStack itemstack) {
                boolean wasEmpty = getStackInSlot(slotId).isEmpty();
                super.setInventorySlotContents(slotId, itemstack);
                boolean isEmpty = itemstack.isEmpty();
                if (wasEmpty != isEmpty) {
                    getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos())
                            .with(BlockItemCapabilityProxy.INACTIVE, isEmpty));
                } else {
                    // Trigger a block update anyway, so nearby blocks can recheck capabilities.
                    BlockHelpers.markForUpdate(getWorld(), getPos());
                }
                invalidateCapsCached();
            }
        };
        addCapabilityInternal(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, LazyOptional.of(getInventory()::getItemHandler));
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.inventory.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        this.inventory.write(tag);
        return super.write(tag);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    public Direction getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getWorld().getBlockState(getPos()), BlockItemCapabilityProxy.FACING, Direction.UP);
    }

    protected ItemStack getContents() {
        return this.inventory.getStackInSlot(0);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            capability = (Capability<T>) CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
        }
        if (facing == getFacing()) {
            return super.getCapability(capability, facing);
        }
        ItemStack itemStack = getContents();
        Capability<T> finalCapability = capability;
        return TileCapabilityProxy.getCapabilityCached(cachedCapabilities, capability, "",
                () -> itemStack.getCapability(finalCapability, facing));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.capabilityproxy.item_capability_proxy");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new ContainerItemCapabilityProxy(id, playerInventory, this.getInventory());
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        invalidateCapsCached();
    }

    protected void invalidateCapsCached() {
        for (LazyOptional<?> value : cachedCapabilities.values()) {
            value.invalidate();
        }
        cachedCapabilities.clear();
    }
}
