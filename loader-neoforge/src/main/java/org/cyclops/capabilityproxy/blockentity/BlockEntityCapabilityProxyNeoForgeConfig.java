package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

import java.util.List;

/**
 * Config for the {@link BlockEntityCapabilityProxyNeoForge}.
 * @author rubensworks
 *
 */
public class BlockEntityCapabilityProxyNeoForgeConfig extends BlockEntityConfigCommon<BlockEntityCapabilityProxyNeoForge, CapabilityProxyNeoForge> {

    @ConfigurablePropertyCommon(category = "machine", comment = "Names of capabilities that are not marked as proxyable, but must be proxied nonetheless.", requiresMcRestart = true, configLocation = ModConfigLocation.SERVER)
    public static List<String> capabilitiesForceProxable = Lists.newArrayList();

    public BlockEntityCapabilityProxyNeoForgeConfig() {
        super(
                CapabilityProxyNeoForge._instance,
                "capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityCapabilityProxyNeoForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_CAPABILITY_PROXY.value()))
        );
        CapabilityProxyNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (BlockCapability<?, ?> blockCapability : BlockCapability.getAll()) {
            if (CapabilityProxyNeoForge.shouldRegisterCapability(blockCapability, capabilitiesForceProxable)) {
                event.registerBlockEntity(
                        (BlockCapability) blockCapability, getInstance(),
                        (blockEntity, context) -> blockEntity.getCapability((BlockCapability) blockCapability, context)
                );
            }
        }
    }

}
