package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxyNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityRangedCapabilityProxyNeoForge}.
 * @author rubensworks
 *
 */
public class BlockEntityRangedCapabilityProxyNeoForgeConfig extends BlockEntityConfigCommon<BlockEntityRangedCapabilityProxyNeoForge, CapabilityProxyNeoForge> {

    public BlockEntityRangedCapabilityProxyNeoForgeConfig() {
        super(
                CapabilityProxyNeoForge._instance,
                "ranged_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityRangedCapabilityProxyNeoForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()))
        );
        CapabilityProxyNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (BlockCapability<?, ?> blockCapability : BlockCapability.getAll()) {
            event.registerBlockEntity(
                    (BlockCapability) blockCapability, getInstance(),
                    (blockEntity, context) -> blockEntity.getCapability((BlockCapability) blockCapability, context)
            );
        }
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            registerClientSide();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerClientSide() {
        CapabilityProxyNeoForge._instance.getProxy().registerRenderer(getInstance(), RenderTileRangedCapabilityProxyNeoForge::new);
    }

}
