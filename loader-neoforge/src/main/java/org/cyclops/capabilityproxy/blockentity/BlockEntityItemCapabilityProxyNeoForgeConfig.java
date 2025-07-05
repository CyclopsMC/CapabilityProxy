package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

import java.util.List;
import java.util.Map;

/**
 * Config for the {@link BlockEntityItemCapabilityProxyNeoForge}.
 * @author rubensworks
 *
 */
public class BlockEntityItemCapabilityProxyNeoForgeConfig extends BlockEntityConfigCommon<BlockEntityItemCapabilityProxyNeoForge, CapabilityProxyNeoForge> {

    @ConfigurablePropertyCommon(category = "machine", comment = "Names of capabilities that are not marked as proxyable, but must be proxied nonetheless.", requiresMcRestart = true, configLocation = ModConfigLocation.SERVER)
    public static List<String> capabilitiesForceProxable = Lists.newArrayList();

    public BlockEntityItemCapabilityProxyNeoForgeConfig() {
        super(
                CapabilityProxyNeoForge._instance,
                "item_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityItemCapabilityProxyNeoForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY.value()))
        );
        CapabilityProxyNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Reset block to entity cap mapping
        BlockEntityItemCapabilityProxyNeoForge.BLOCK_TO_ITEM_CAPABILITIES = Maps.newIdentityHashMap();

        // A temporary map for quickly getting item caps by name
        Map<String, ItemCapability> namedItemCapabilities = Maps.newHashMap();
        for (ItemCapability<?, ?> itemCapability : ItemCapability.getAll()) {
            namedItemCapabilities.put(itemCapability.name().toString(), itemCapability);
        }

        for (BlockCapability<?, ?> blockCapability : BlockCapability.getAll()) {
            if (CapabilityProxyNeoForge.shouldRegisterCapability(blockCapability, capabilitiesForceProxable)) {
                event.registerBlockEntity(
                        (BlockCapability) blockCapability, getInstance(),
                        (object, context) -> object.getCapability((BlockCapability) blockCapability, context)
                );

                // Heuristically try to match block caps with item caps
                ItemCapability itemCapability = namedItemCapabilities.get(blockCapability.name().toString());
                if (itemCapability != null) {
                    BlockEntityItemCapabilityProxyNeoForge.BLOCK_TO_ITEM_CAPABILITIES.put(blockCapability, itemCapability);
                }
            }
        }
    }

}
