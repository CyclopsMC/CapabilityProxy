package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxyForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityCapabilityProxyForge}.
 * @author rubensworks
 *
 */
public class BlockEntityCapabilityProxyForgeConfig extends BlockEntityConfigCommon<BlockEntityCapabilityProxyForge, CapabilityProxyForge> {

    public BlockEntityCapabilityProxyForgeConfig() {
        super(
                CapabilityProxyForge._instance,
                "capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityCapabilityProxyForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_CAPABILITY_PROXY.value()), null)
        );
    }

}
