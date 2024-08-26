package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.fluid.FluidHandlerWrapperForge;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * An item capability proxy.
 * @author rubensworks
 */
public class BlockEntityItemCapabilityProxyForge extends BlockEntityItemCapabilityProxyCommon {

    private final Map<Pair<String, Capability<?>>, LazyOptional<?>> cachedCapabilities = Maps.newHashMap();

    public BlockEntityItemCapabilityProxyForge(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    protected void onInventoryChanged() {
        super.onInventoryChanged();
        invalidateCapsCached();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.of(() -> (T) new InvWrapper(getInventory()));
        }

        // Check if we are handling the fluid capability
        boolean transformFluidCapability = capability == ForgeCapabilities.FLUID_HANDLER;
        if (transformFluidCapability) {
            capability = (Capability<T>) ForgeCapabilities.FLUID_HANDLER_ITEM;
        }

        if (facing == getFacing()) {
            return super.getCapability(capability, facing);
        }
        ItemStack itemStack = getContents();
        Capability<T> finalCapability = capability;
        return BlockEntityCapabilityProxyForge.getCapabilityCached(cachedCapabilities, capability, "",
                () -> {
                    LazyOptional<T> cap = getItemStackCapability(finalCapability, itemStack, facing);

                    // Modify fluid capability instance so that the container is always updated into this tile after modifications
                    if (transformFluidCapability) {
                        cap = cap.<IFluidHandlerItem>cast()
                                .lazyMap(fluidHandler -> new FluidHandlerWrapperItem(fluidHandler, this))
                                .cast();
                    }
                    return cap;
                });
    }

    // TODO: remove when Forge implements capabilities on items
    public static <T> LazyOptional<T> getItemStackCapability(Capability<T> capability, ItemStack itemStack, Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER_ITEM && itemStack.getItem() instanceof BucketItem) {
            return LazyOptional.of(() -> (T) new FluidBucketWrapper(itemStack));
        }
        return LazyOptional.empty();
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

    public static class FluidHandlerWrapperItem extends FluidHandlerWrapperForge implements IFluidHandlerItem {

        private final IFluidHandlerItem fluidHandler;
        private final BlockEntityItemCapabilityProxyForge tile;

        public FluidHandlerWrapperItem(IFluidHandlerItem fluidHandler, BlockEntityItemCapabilityProxyForge tile) {
            super(fluidHandler);
            this.fluidHandler = fluidHandler;
            this.tile = tile;
        }

        protected void updateContainerSlot() {
            tile.getInventory().setItem(0, getContainer());
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
