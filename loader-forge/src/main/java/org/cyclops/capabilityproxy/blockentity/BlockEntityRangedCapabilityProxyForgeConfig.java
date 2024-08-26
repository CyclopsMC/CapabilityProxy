package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.capabilityproxy.CapabilityProxyForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityRangedCapabilityProxyForge}.
 * @author rubensworks
 *
 */
public class BlockEntityRangedCapabilityProxyForgeConfig extends BlockEntityConfigCommon<BlockEntityRangedCapabilityProxyForge, CapabilityProxyForge> {

    public BlockEntityRangedCapabilityProxyForgeConfig() {
        super(
                CapabilityProxyForge._instance,
                "ranged_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityRangedCapabilityProxyForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()), null)
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if(getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            registerClientSide();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerClientSide() {
        CapabilityProxyForge._instance.getProxy().registerRenderer(getInstance(), RenderTileRangedCapabilityProxy::new);
    }

}
