package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxyForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityItemCapabilityProxyForge}.
 * @author rubensworks
 *
 */
public class BlockEntityItemCapabilityProxyForgeConfig extends BlockEntityConfigCommon<BlockEntityItemCapabilityProxyForge, CapabilityProxyForge> {

    public BlockEntityItemCapabilityProxyForgeConfig() {
        super(
                CapabilityProxyForge._instance,
                "item_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityItemCapabilityProxyForge::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY.value()), null)
        );
    }

}
