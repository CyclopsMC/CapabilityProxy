package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxy;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.fluid.FluidHandlerWrapper;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * An item capability proxy.
 * @author rubensworks
 */
public class BlockEntityItemCapabilityProxy extends CyclopsBlockEntity implements MenuProvider {

    private final SimpleInventory inventory;
    private final Map<Pair<String, Capability<?>>, LazyOptional<?>> cachedCapabilities = Maps.newHashMap();

    public BlockEntityItemCapabilityProxy(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_ITEM_CAPABILITY_PROXY, blockPos, blockState);
        this.inventory = new SimpleInventory(1, 1) {
            @Override
            public void setItem(int slotId, ItemStack itemstack) {
                boolean wasEmpty = getItem(slotId).isEmpty();
                super.setItem(slotId, itemstack);
                boolean isEmpty = itemstack.isEmpty();
                if (wasEmpty != isEmpty) {
                    getLevel().setBlockAndUpdate(getBlockPos(), getLevel().getBlockState(getBlockPos())
                            .setValue(BlockItemCapabilityProxy.INACTIVE, isEmpty));
                } else {
                    // Trigger a block update anyway, so nearby blocks can recheck capabilities.
                    BlockHelpers.markForUpdate(getLevel(), getBlockPos());
                }
                invalidateCapsCached();
            }
        };
        addCapabilityInternal(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, LazyOptional.of(getInventory()::getItemHandler));
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        this.inventory.read(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.inventory.write(tag);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    public Direction getFacing() {
        return BlockHelpers.getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockItemCapabilityProxy.FACING, Direction.UP);
    }

    protected ItemStack getContents() {
        return this.inventory.getItem(0);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        // Check if we are handling the fluid capability
        boolean transformFluidCapability = capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
        if (transformFluidCapability) {
            capability = (Capability<T>) CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
        }

        if (facing == getFacing()) {
            return super.getCapability(capability, facing);
        }
        ItemStack itemStack = getContents();
        Capability<T> finalCapability = capability;
        return BlockEntityCapabilityProxy.getCapabilityCached(cachedCapabilities, capability, "",
                () -> {
                    LazyOptional<T> cap = itemStack.getCapability(finalCapability, facing);

                    // Modify fluid capability instance so that the container is always updated into this tile after modifications
                    if (transformFluidCapability) {
                        cap = cap.<IFluidHandlerItem>cast()
                                .lazyMap(fluidHandler -> new FluidHandlerWrapperItem(fluidHandler, this))
                                .cast();
                    }
                    return cap;
                });
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.capabilityproxy.item_capability_proxy");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ContainerItemCapabilityProxy(id, playerInventory, this.getInventory());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        invalidateCapsCached();
    }

    protected void invalidateCapsCached() {
        for (LazyOptional<?> value : cachedCapabilities.values()) {
            value.invalidate();
        }
        cachedCapabilities.clear();
    }

    public static class FluidHandlerWrapperItem extends FluidHandlerWrapper implements IFluidHandlerItem {

        private final IFluidHandlerItem fluidHandler;
        private final BlockEntityItemCapabilityProxy tile;

        public FluidHandlerWrapperItem(IFluidHandlerItem fluidHandler, BlockEntityItemCapabilityProxy tile) {
            super(fluidHandler);
            this.fluidHandler = fluidHandler;
            this.tile = tile;
        }

        protected void updateContainerSlot() {
            tile.inventory.setItem(0, getContainer());
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int ret = super.fill(resource, action);
            if (action.execute()) {
                updateContainerSlot();
            }
            return ret;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack ret = super.drain(maxDrain, action);
            if (action.execute()) {
                updateContainerSlot();
            }
            return ret;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack ret = super.drain(resource, action);
            if (action.execute()) {
                updateContainerSlot();
            }
            return ret;
        }

        @Nonnull
        @Override
        public ItemStack getContainer() {
            return this.fluidHandler.getContainer();
        }
    }
}
