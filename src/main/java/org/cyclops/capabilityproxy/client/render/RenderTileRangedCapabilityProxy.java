package org.cyclops.capabilityproxy.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.cyclops.capabilityproxy.Reference;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityRangedCapabilityProxy;

import java.util.OptionalDouble;

/**
 * Renders an overlay showing the target of ranged proxies when a ranged proxy is held in hand.
 * @author rubensworks
 */
public class RenderTileRangedCapabilityProxy implements BlockEntityRenderer<BlockEntityRangedCapabilityProxy> {

    public static final RenderType RENDER_TYPE_LINE = RenderType.create(Reference.MOD_ID + "line",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINES, 128, false, false, RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(1)))
                    .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .createCompositeState(false));

    public RenderTileRangedCapabilityProxy(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public AABB getRenderBoundingBox(BlockEntityRangedCapabilityProxy blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(BlockRangedCapabilityProxyConfig.range);
    }

    @Override
    public void render(BlockEntityRangedCapabilityProxy tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Player player = Minecraft.getInstance().player;
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY.get()
                || player.getItemInHand(InteractionHand.OFF_HAND).getItem() == RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY.get()) {
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

            VertexConsumer vb = buffer.getBuffer(RENDER_TYPE_LINE);
            vb.addVertex(matrixStack.last().pose(), minX, minY, minZ).setColor(r, g, b, a);
            vb.addVertex(matrixStack.last().pose(), maxX, maxY, maxZ).setColor(r, g, b, a);
        }
    }

    @Override
    public boolean shouldRenderOffScreen(BlockEntityRangedCapabilityProxy te) {
        return true;
    }
}
