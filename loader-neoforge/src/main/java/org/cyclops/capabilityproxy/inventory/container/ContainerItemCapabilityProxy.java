package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.container.InventoryContainer;

/**
 * Container for the item capability proxy.
 * @author rubensworks
 */
public class ContainerItemCapabilityProxy extends InventoryContainer {

    public ContainerItemCapabilityProxy(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleInventory(1, 1));
    }

    public ContainerItemCapabilityProxy(int id, Inventory playerInventory, Container inventory) {
        super(RegistryEntries.CONTAINER_ITEM_CAPABILITY_PROXY.get(), id, playerInventory, inventory);
        addSlot(new Slot(inventory, 0, 80, 16));
        addPlayerInventory(playerInventory, offsetX + 8, offsetY + 46);
    }
}
