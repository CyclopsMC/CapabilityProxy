package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Config for the {@link BlockEntityRangedCapabilityProxy}.
 * @author rubensworks
 *
 */
public class BlockEntityRangedCapabilityProxyConfig extends BlockEntityConfig<BlockEntityRangedCapabilityProxy> {

    public BlockEntityRangedCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "ranged_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityRangedCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.get()), null)
        );
        CapabilityProxy._instance.getModEventBus().addListener(this::registerCapabilities);
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
        if(MinecraftHelpers.isClientSide()) {
            registerClientSide();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerClientSide() {
        CapabilityProxy._instance.getProxy().registerRenderer(getInstance(), RenderTileRangedCapabilityProxy::new);
    }

}
