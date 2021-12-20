package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;

/**
 * Config for the {@link BlockEntityItemCapabilityProxy}.
 * @author rubensworks
 *
 */
public class BlockEntityItemCapabilityProxyConfig extends BlockEntityConfig<BlockEntityItemCapabilityProxy> {

    public BlockEntityItemCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "item_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityItemCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY), null)
        );
    }

}
