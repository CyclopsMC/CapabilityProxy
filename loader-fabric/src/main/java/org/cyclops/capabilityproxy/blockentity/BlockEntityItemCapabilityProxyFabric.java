package org.cyclops.capabilityproxy.blockentity;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * An item capability proxy.
 * @author rubensworks
 */
public class BlockEntityItemCapabilityProxyFabric extends BlockEntityItemCapabilityProxyCommon {

    public static Map<BlockApiLookup<?, ?>, ItemApiLookup<?, ?>> BLOCK_TO_ITEM_CAPABILITIES;

    public BlockEntityItemCapabilityProxyFabric(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
        BlockEntityItemCapabilityProxyFabricConfig.itemApiRegistrar.initializeCapabilityRegistrationsIfNeeded((BlockEntityType<? extends BlockEntityItemCapabilityProxyFabric>) getType());
    }

    public <T, C1, C2> T getCapability(BlockApiLookup<T, C1> blockCapability, C1 context) {
        if (context instanceof Direction && context == getFacing() && blockCapability == ItemStorage.SIDED) {
            return (T) InventoryStorage.of(getInventory(), (Direction) context);
        }

        // Convert block cap to item cap
        ItemStack itemStack = getContents();
        ItemApiLookup<T, C2> itemCapability = blockCapabilityToItemCapability(blockCapability);
        if (itemCapability == null) {
            return null;
        }

        // Determine item context
        C2 itemContext = null;
        if (itemCapability.contextClass() == Direction.class) {
            itemContext = (C2) getFacing().getOpposite();
        }
        if (itemCapability.contextClass() == ContainerItemContext.class) {
            itemContext = (C2) ContainerItemContext.ofSingleSlot(InventoryStorage.of(getInventory(), (Direction) context).getSlot(0));
        }

        // Retrieve cap
        return itemCapability.find(itemStack, itemContext);
    }

    @Nullable
    public static <T, C1, C2> ItemApiLookup<T, C2> blockCapabilityToItemCapability(BlockApiLookup<T, C1> capability) {
        // Custom override for fluid storage
        if (capability == FluidStorage.SIDED) {
            return (ItemApiLookup<T, C2>) FluidStorage.ITEM;
        }
        return (ItemApiLookup<T, C2>) BLOCK_TO_ITEM_CAPABILITIES.get(capability);
    }
}
