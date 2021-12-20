package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;

/**
 * Config for the {@link BlockEntityEntityCapabilityProxy}.
 * @author rubensworks
 *
 */
public class BlockEntityEntityCapabilityProxyConfig extends BlockEntityConfig<BlockEntityEntityCapabilityProxy> {

    public BlockEntityEntityCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "entity_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityEntityCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY), null)
        );
    }

}
