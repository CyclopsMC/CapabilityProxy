package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;

/**
 * Config for the {@link BlockEntityCapabilityProxy}.
 * @author rubensworks
 *
 */
public class BlockEntityCapabilityProxyConfig extends BlockEntityConfig<BlockEntityCapabilityProxy> {

    public BlockEntityCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_CAPABILITY_PROXY), null)
        );
    }

}
