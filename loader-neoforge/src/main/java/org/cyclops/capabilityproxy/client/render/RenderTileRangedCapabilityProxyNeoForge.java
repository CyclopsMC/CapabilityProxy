package org.cyclops.capabilityproxy.client.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyCommon;

/**
 * @author rubensworks
 */
public class RenderTileRangedCapabilityProxyNeoForge extends RenderTileRangedCapabilityProxy {
    public RenderTileRangedCapabilityProxyNeoForge(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public AABB getRenderBoundingBox(BlockEntityCapabilityProxyCommon blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(BlockRangedCapabilityProxyConfig.range);
    }
}
