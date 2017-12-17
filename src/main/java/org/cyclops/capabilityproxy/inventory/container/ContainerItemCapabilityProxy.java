package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import org.cyclops.capabilityproxy.tileentity.TileItemCapabilityProxy;
import org.cyclops.cyclopscore.inventory.container.TileInventoryContainerConfigurable;

/**
 * Container for the item capability proxy.
 * @author rubensworks
 */
public class ContainerItemCapabilityProxy extends TileInventoryContainerConfigurable<TileItemCapabilityProxy> {

    /**
     * Make a new instance.
     * @param inventory The player inventory.
     * @param tile The part.
     */
    public ContainerItemCapabilityProxy(InventoryPlayer inventory, TileItemCapabilityProxy tile) {
        super(inventory, tile);
        addSlotToContainer(new Slot(tile, 0, 80, 16));
        addPlayerInventory(inventory, offsetX + 8, offsetY + 46);
    }
}
