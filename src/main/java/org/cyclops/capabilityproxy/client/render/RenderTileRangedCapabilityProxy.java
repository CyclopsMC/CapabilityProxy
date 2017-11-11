package org.cyclops.capabilityproxy.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxy;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;

/**
 * Renders an overlay showing the target of ranged proxies when a ranged proxy is held in hand.
 * @author rubensworks
 */
public class RenderTileRangedCapabilityProxy extends TileEntitySpecialRenderer<TileRangedCapabilityProxy> {

    @Override
    public void render(TileRangedCapabilityProxy te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (Block.getBlockFromItem(player.getHeldItem(EnumHand.MAIN_HAND).getItem()) == BlockRangedCapabilityProxy.getInstance()
                || Block.getBlockFromItem(player.getHeldItem(EnumHand.OFF_HAND).getItem()) == BlockRangedCapabilityProxy.getInstance()) {
            float r = 0.28F;
            float g = 0.87F;
            float b = 0.80F;
            float a = 0.60F;

            x += 0.5F;
            y += 0.5F;
            z += 0.5F;

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth((float) (1 / (Math.abs(x) + Math.abs(y) + Math.abs(z) + 1)));
            GlStateManager.disableTexture2D();
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
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileRangedCapabilityProxy te) {
        return true;
    }
}
