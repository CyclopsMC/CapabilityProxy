package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
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

    public static Map<BlockCapability<?, ?>, ItemCapability<?, ?>> BLOCK_TO_ITEM_CAPABILITIES;

    private final SimpleInventory inventory;

    public BlockEntityItemCapabilityProxy(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_ITEM_CAPABILITY_PROXY.get(), blockPos, blockState);
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
                level.invalidateCapabilities(getBlockPos());
            }
        };
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

    public <T, C1, C2> T getCapability(BlockCapability<T, C1> blockCapability, C1 context) {
        if (context instanceof Direction && context == getFacing() && blockCapability == Capabilities.ItemHandler.BLOCK) {
            return (T) getInventory().getItemHandler();
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

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.capabilityproxy.item_capability_proxy");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ContainerItemCapabilityProxy(id, playerInventory, this.getInventory());
    }

    @Nullable
    public static <T, C1, C2> ItemCapability<T, C2> blockCapabilityToItemCapability(BlockCapability<T, C1> capability) {
        return (ItemCapability<T, C2>) BLOCK_TO_ITEM_CAPABILITIES.get(capability);
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
