package org.cyclops.capabilityproxy.block;

import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;

/**
 * Config for {@link BlockEntityCapabilityProxy}.
 * @author josephcsible
 */
public class BlockEntityCapabilityProxyConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static BlockEntityCapabilityProxyConfig _instance;

    /**
     * Make a new instance.
     */
    public BlockEntityCapabilityProxyConfig() {
        super(
            CapabilityProxy._instance,
            true,
            "entity_capability_proxy",
            null,
            BlockEntityCapabilityProxy.class
        );
    }

    @Override
    public boolean isDisableable() {
        return true;
    }

}
