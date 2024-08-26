package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxyForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityEntityCapabilityProxyForge}.
 * @author rubensworks
 *
 */
public class BlockEntityEntityCapabilityProxyForgeConfig extends BlockEntityConfigCommon<BlockEntityEntityCapabilityProxyForge, CapabilityProxyForge> {

    public BlockEntityEntityCapabilityProxyForgeConfig() {
        super(
                CapabilityProxyForge._instance,
                "entity_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityEntityCapabilityProxyForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY.value()), null)
        );
    }

}
