package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.cyclops.cyclopscore.fluid.FluidHandlerWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * An item capability proxy.
 * @author rubensworks
 */
public class BlockEntityItemCapabilityProxyNeoForge extends BlockEntityItemCapabilityProxyCommon {

    public static Map<BlockCapability<?, ?>, ItemCapability<?, ?>> BLOCK_TO_ITEM_CAPABILITIES;

    public BlockEntityItemCapabilityProxyNeoForge(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    protected void onInventoryChanged() {
        super.onInventoryChanged();
        level.invalidateCapabilities(getBlockPos());
    }

    public <T, C1, C2> T getCapability(BlockCapability<T, C1> blockCapability, C1 context) {
        if (context instanceof Direction && context == getFacing() && blockCapability == Capabilities.ItemHandler.BLOCK) {
            return (T) new InvWrapper(getInventory());
        }

        ItemStack itemStack = getContents();
        ItemCapability<T, C2> itemCapability = blockCapabilityToItemCapability(blockCapability);
        if (itemCapability == null) {
            return null;
        }
        T cap = itemStack.getCapability(itemCapability, itemCapability.contextClass() == Direction.class ? (C2) getFacing().getOpposite() : null);

        // Modify fluid capability instance so that the container is always updated into this tile after modifications
        if (cap instanceof IFluidHandlerItem fluidHandlerItem) {
            cap = (T) new FluidHandlerWrapperItem(fluidHandlerItem, this);
        }
        return cap;
    }

    @Nullable
    public static <T, C1, C2> ItemCapability<T, C2> blockCapabilityToItemCapability(BlockCapability<T, C1> capability) {
        return (ItemCapability<T, C2>) BLOCK_TO_ITEM_CAPABILITIES.get(capability);
    }

    public static class FluidHandlerWrapperItem extends FluidHandlerWrapper implements IFluidHandlerItem {

        private final IFluidHandlerItem fluidHandler;
        private final BlockEntityItemCapabilityProxyNeoForge tile;

        public FluidHandlerWrapperItem(IFluidHandlerItem fluidHandler, BlockEntityItemCapabilityProxyNeoForge tile) {
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
