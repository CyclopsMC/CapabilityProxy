package org.cyclops.capabilityproxy.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.capabilityproxy.Reference;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended;

/**
 * Gui for the item capability proxy.
 * @author rubensworks
 */
public class ContainerScreenItemCapabilityProxy extends ContainerScreenExtended<ContainerItemCapabilityProxy> {

    public ContainerScreenItemCapabilityProxy(ContainerItemCapabilityProxy container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }

    @Override
    protected ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/item_capability_proxy.png");
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }
}
