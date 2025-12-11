package org.cyclops.capabilityproxy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.cyclops.capabilityproxy.Reference;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyCommon;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalDouble;

/**
 * Renders an overlay showing the target of ranged proxies when a ranged proxy is held in hand.
 * @author rubensworks
 */
public class RenderTileRangedCapabilityProxy implements BlockEntityRenderer<BlockEntityCapabilityProxyCommon, RenderTileRangedCapabilityProxy.RenderState> {

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
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(BlockEntityCapabilityProxyCommon blockEntity, RenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.facing = blockEntity.getFacing();
    }

    @Override
    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
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

            BlockPos target = new BlockPos(0, 0, 0).relative(renderState.facing, BlockRangedCapabilityProxyConfig.range);
            float minX = x;
            float minY = y;
            float minZ = z;
            float maxX = x + target.getX();
            float maxY = y + target.getY();
            float maxZ = z + target.getZ();

            submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE_LINE, (pose, vertexConsumer) -> {
                vertexConsumer.addVertex(pose, minX, minY, minZ).setColor(r, g, b, a).setNormal(0.0F, 0.0F, 0.0F);
                vertexConsumer.addVertex(pose, maxX, maxY, maxZ).setColor(r, g, b, a).setNormal(0.0F, 0.0F, 0.0F);;
            });
        }
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    public static class RenderState extends BlockEntityRenderState {
        public Direction facing = Direction.NORTH;
    }
}
