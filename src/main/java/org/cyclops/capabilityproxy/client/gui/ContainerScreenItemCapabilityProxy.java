package org.cyclops.capabilityproxy.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.cyclops.capabilityproxy.Reference;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended;

/**
 * Gui for the item capability proxy.
 * @author rubensworks
 */
public class ContainerScreenItemCapabilityProxy extends ContainerScreenExtended<ContainerItemCapabilityProxy> {

    public ContainerScreenItemCapabilityProxy(ContainerItemCapabilityProxy container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    protected ResourceLocation constructGuiTexture() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/item_capability_proxy.png");
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        //super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        this.font.draw(matrixStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        // TODO: rm
    }
}
