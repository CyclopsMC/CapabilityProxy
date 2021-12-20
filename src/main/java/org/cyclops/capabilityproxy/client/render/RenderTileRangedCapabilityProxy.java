package org.cyclops.capabilityproxy.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.cyclops.capabilityproxy.Reference;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

/**
 * Renders an overlay showing the target of ranged proxies when a ranged proxy is held in hand.
 * @author rubensworks
 */
public class RenderTileRangedCapabilityProxy extends TileEntityRenderer<TileRangedCapabilityProxy> {

    public static final RenderType RENDER_TYPE_LINE = RenderType.create(Reference.MOD_ID + "line",
            DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, RenderType.State.builder()
                    .setLineState(new RenderState.LineState(OptionalDouble.of(1)))
                    .setLayeringState(RenderState.VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(RenderState.TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(new RenderState.WriteMaskState(true, false))
                    .createCompositeState(false));

    public RenderTileRangedCapabilityProxy(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileRangedCapabilityProxy tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player.getItemInHand(Hand.MAIN_HAND).getItem() == RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY
                || player.getItemInHand(Hand.OFF_HAND).getItem() == RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY) {
            float r = 0.28F;
            float g = 0.87F;
            float b = 0.80F;
            float a = 0.60F;

            float x = 0.5F;
            float y = 0.5F;
            float z = 0.5F;

            BlockPos target = new BlockPos(0, 0, 0).relative(tile.getFacing(), BlockRangedCapabilityProxyConfig.range);
            float minX = x;
            float minY = y;
            float minZ = z;
            float maxX = x + target.getX();
            float maxY = y + target.getY();
            float maxZ = z + target.getZ();

            IVertexBuilder vb = buffer.getBuffer(RENDER_TYPE_LINE);
            vb.vertex(matrixStack.last().pose(), minX, minY, minZ).color(r, g, b, a).endVertex();
            vb.vertex(matrixStack.last().pose(), maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(TileRangedCapabilityProxy te) {
        return true;
    }
}
