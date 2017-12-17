package org.cyclops.capabilityproxy.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileItemCapabilityProxy;
import org.cyclops.cyclopscore.client.gui.container.GuiContainerConfigurable;

/**
 * Gui for the item capability proxy.
 * @author rubensworks
 */
public class GuiItemCapabilityProxy extends GuiContainerConfigurable<ContainerItemCapabilityProxy> {

    /**
     * Make a new instance.
     * @param inventory The player inventory.
     * @param tile The tile.
     */
    public GuiItemCapabilityProxy(InventoryPlayer inventory, TileItemCapabilityProxy tile) {
        super(new ContainerItemCapabilityProxy(inventory, tile));
    }
}
