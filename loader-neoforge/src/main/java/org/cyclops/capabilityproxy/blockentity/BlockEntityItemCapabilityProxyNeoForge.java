package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

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
        ResourceHandler<ItemResource> inventoryHandler = VanillaContainerWrapper.of(getInventory());
        if (context instanceof Direction && context == getFacing() && blockCapability == Capabilities.Item.BLOCK) {
             return (T) inventoryHandler;
        }

        ItemStack itemStack = getContents();
        ItemCapability<T, C2> itemCapability = blockCapabilityToItemCapability(blockCapability);
        if (itemCapability == null) {
            return null;
        }
        return itemStack.getCapability(itemCapability, itemCapability.contextClass() == ItemAccess.class ? (C2) ItemAccess.forHandlerIndexStrict(inventoryHandler, 0) : null);
    }

    @Nullable
    public static <T, C1, C2> ItemCapability<T, C2> blockCapabilityToItemCapability(BlockCapability<T, C1> capability) {
        return (ItemCapability<T, C2>) BLOCK_TO_ITEM_CAPABILITIES.get(capability);
    }
}
