package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.inventory.SimpleInventoryCommon;
import org.cyclops.cyclopscore.inventory.container.InventoryContainerCommon;

/**
 * Container for the item capability proxy.
 * @author rubensworks
 */
public class ContainerItemCapabilityProxy extends InventoryContainerCommon {

    public ContainerItemCapabilityProxy(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleInventoryCommon(1, 1));
    }

    public ContainerItemCapabilityProxy(int id, Inventory playerInventory, Container inventory) {
        super(RegistryEntries.CONTAINER_ITEM_CAPABILITY_PROXY.value(), id, playerInventory, inventory);
        addSlot(new Slot(inventory, 0, 80, 16));
        addPlayerInventory(playerInventory, offsetX + 8, offsetY + 46);
    }
}
