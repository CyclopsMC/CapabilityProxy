package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;

/**
 * Config for the {@link BlockEntityCapabilityProxyNeoForge}.
 * @author rubensworks
 *
 */
public class BlockEntityCapabilityProxyNeoForgeConfig extends BlockEntityConfig<BlockEntityCapabilityProxyNeoForge> {

    public BlockEntityCapabilityProxyNeoForgeConfig() {
        super(
                CapabilityProxyNeoForge._instance,
                "capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityCapabilityProxyNeoForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_CAPABILITY_PROXY.value()), null)
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

}
