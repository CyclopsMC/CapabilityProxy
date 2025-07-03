package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxy;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;

import javax.annotation.Nullable;

/**
 * An item capability proxy.
 * @author rubensworks
 */
public class BlockEntityItemCapabilityProxyCommon extends CyclopsBlockEntity implements MenuProvider {

    private final SimpleInventory inventory;

    public BlockEntityItemCapabilityProxyCommon(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.TILE_ENTITY_ITEM_CAPABILITY_PROXY.value(), blockPos, blockState);
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
                    IModHelpers.get().getBlockHelpers().markForUpdate(getLevel(), getBlockPos());
                }
                BlockEntityItemCapabilityProxyCommon.this.onInventoryChanged();
            }
        };
    }

    protected void onInventoryChanged() {

    }

    @Override
    public void read(ValueInput input) {
        super.read(input);
        this.inventory.read(input);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.inventory.write(output);
    }

    public SimpleInventory getInventory() {
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
