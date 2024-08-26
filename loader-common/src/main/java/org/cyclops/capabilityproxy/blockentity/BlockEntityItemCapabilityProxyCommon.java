package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxy;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventoryCommon;

import javax.annotation.Nullable;

/**
 * An item capability proxy.
 * @author rubensworks
 */
public class BlockEntityItemCapabilityProxyCommon extends CyclopsBlockEntityCommon implements MenuProvider {

    private final SimpleInventoryCommon inventory;

    public BlockEntityItemCapabilityProxyCommon(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_ITEM_CAPABILITY_PROXY.value(), blockPos, blockState);
        this.inventory = new SimpleInventoryCommon(1, 1) {
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
                    IModHelpers.get().getBlockHelpers().markForUpdate(getLevel(), getBlockPos());
                }
                BlockEntityItemCapabilityProxyCommon.this.onInventoryChanged();
            }
        };
    }

    protected void onInventoryChanged() {

    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider provider) {
        super.read(tag, provider);
        this.inventory.read(provider, tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        this.inventory.write(provider, tag);
    }

    public SimpleInventoryCommon getInventory() {
        return inventory;
    }

    public Direction getFacing() {
        return IModHelpers.get().getBlockHelpers().getSafeBlockStateProperty(getLevel().getBlockState(getBlockPos()), BlockItemCapabilityProxy.FACING, Direction.UP);
    }

    protected ItemStack getContents() {
        return this.inventory.getItem(0);
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
}
