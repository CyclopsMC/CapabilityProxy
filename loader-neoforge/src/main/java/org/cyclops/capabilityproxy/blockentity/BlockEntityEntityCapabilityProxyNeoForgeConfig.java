package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

import java.util.List;
import java.util.Map;

/**
 * Config for the {@link BlockEntityEntityCapabilityProxyNeoForge}.
 * @author rubensworks
 *
 */
public class BlockEntityEntityCapabilityProxyNeoForgeConfig extends BlockEntityConfigCommon<BlockEntityEntityCapabilityProxyNeoForge, CapabilityProxyNeoForge> {

    @ConfigurablePropertyCommon(category = "machine", comment = "Names of capabilities that are not marked as proxyable, but must be proxied nonetheless.", requiresMcRestart = true, configLocation = ModConfigLocation.SERVER)
    public static List<String> capabilitiesForceProxable = Lists.newArrayList();

    public BlockEntityEntityCapabilityProxyNeoForgeConfig() {
        super(
                CapabilityProxyNeoForge._instance,
                "entity_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityEntityCapabilityProxyNeoForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY.value()))
        );
        CapabilityProxyNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Reset block to entity cap mapping
        BlockEntityEntityCapabilityProxyNeoForge.BLOCK_TO_ENTITY_CAPABILITIES = Maps.newIdentityHashMap();

        // A temporary map for quickly getting entity caps by name
        Map<String, EntityCapability> namedEntityCapabilities = Maps.newHashMap();
        for (EntityCapability<?, ?> entityCapability : EntityCapability.getAll()) {
            namedEntityCapabilities.put(entityCapability.name().toString(), entityCapability);
        }

        for (BlockCapability<?, ?> blockCapability : BlockCapability.getAll()) {
            if (CapabilityProxyNeoForge.shouldRegisterCapability(blockCapability, capabilitiesForceProxable)) {
                event.registerBlockEntity(
                        (BlockCapability) blockCapability, getInstance(),
                        (object, context) -> object.getCapability((BlockCapability) blockCapability)
                );

                // Heuristically try to match block caps with entity caps
                EntityCapability entityCapability = namedEntityCapabilities.get(blockCapability.name().toString());
                if (entityCapability != null) {
                    BlockEntityEntityCapabilityProxyNeoForge.BLOCK_TO_ENTITY_CAPABILITIES.put(blockCapability, entityCapability);
                }
            }
        }
    }

}
