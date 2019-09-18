package org.cyclops.capabilityproxy.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;

/**
 * Renders an overlay showing the target of ranged proxies when a ranged proxy is held in hand.
 * @author rubensworks
 */
public class RenderTileRangedCapabilityProxy extends TileEntityRenderer<TileRangedCapabilityProxy> {

    @Override
    public void render(TileRangedCapabilityProxy te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.render(te, x, y, z, partialTicks, destroyStage);
        PlayerEntity player = Minecraft.getInstance().player;
        if (player.getHeldItem(Hand.MAIN_HAND).getItem() == RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY
                || player.getHeldItem(Hand.OFF_HAND).getItem() == RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY) {
            float r = 0.28F;
            float g = 0.87F;
            float b = 0.80F;
            float a = 0.60F;

            x += 0.5F;
            y += 0.5F;
            z += 0.5F;

            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.lineWidth((float) (1 / (Math.abs(x) + Math.abs(y) + Math.abs(z) + 1)));
            GlStateManager.disableTexture();
            GlStateManager.depthMask(false);

            BlockPos target = new BlockPos(0, 0, 0).offset(te.getFacing(), BlockRangedCapabilityProxyConfig.range);
            double minX = x;
            double minY = y;
            double minZ = z;
            double maxX = x + target.getX();
            double maxY = y + target.getY();
            double maxZ = z + target.getZ();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
            buffer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
            tessellator.draw();

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture();
            GlStateManager.disableBlend();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileRangedCapabilityProxy te) {
        return true;
    }
}
