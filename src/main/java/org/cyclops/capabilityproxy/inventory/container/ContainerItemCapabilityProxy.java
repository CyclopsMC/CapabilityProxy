package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.container.InventoryContainer;

/**
 * Container for the item capability proxy.
 * @author rubensworks
 */
public class ContainerItemCapabilityProxy extends InventoryContainer {

    public ContainerItemCapabilityProxy(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new SimpleInventory(1, 1));
    }

    public ContainerItemCapabilityProxy(int id, PlayerInventory playerInventory, IInventory inventory) {
        super(RegistryEntries.CONTAINER_ITEM_CAPABILITY_PROXY, id, playerInventory, inventory);
        addSlot(new Slot(inventory, 0, 80, 16));
        addPlayerInventory(playerInventory, offsetX + 8, offsetY + 46);
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return false; // TODO: rm
    }
}
