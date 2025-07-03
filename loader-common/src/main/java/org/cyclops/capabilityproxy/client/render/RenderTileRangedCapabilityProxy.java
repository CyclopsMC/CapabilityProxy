package org.cyclops.capabilityproxy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.cyclops.capabilityproxy.Reference;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyCommon;

import java.util.OptionalDouble;

/**
 * Renders an overlay showing the target of ranged proxies when a ranged proxy is held in hand.
 * @author rubensworks
 */
public class RenderTileRangedCapabilityProxy implements BlockEntityRenderer<BlockEntityCapabilityProxyCommon> {

    public static final RenderType RENDER_TYPE_LINE = RenderType.create(Reference.MOD_ID + "line",
            128,
            RenderPipelines.SECONDARY_BLOCK_OUTLINE,
            RenderType.CompositeState.builder()
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(2)))
                    .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                    .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                    .createCompositeState(false));

    public RenderTileRangedCapabilityProxy(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(BlockEntityCapabilityProxyCommon tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, Vec3 cameraPos) {
        Player player = Minecraft.getInstance().player;
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY.value()
                || player.getItemInHand(InteractionHand.OFF_HAND).getItem() == RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY.value()) {
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
            vb.addVertex(matrixStack.last().pose(), minX, minY, minZ).setColor(r, g, b, a).setNormal(0.0F, 0.0F, 0.0F);
            vb.addVertex(matrixStack.last().pose(), maxX, maxY, maxZ).setColor(r, g, b, a).setNormal(0.0F, 0.0F, 0.0F);;
        }
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }
}
