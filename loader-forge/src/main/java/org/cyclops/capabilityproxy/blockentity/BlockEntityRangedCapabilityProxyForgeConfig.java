package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxyForge;
import org.cyclops.capabilityproxy.RegistryEntries;
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
                        Sets.newHashSet(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()))
        );
    }


}
