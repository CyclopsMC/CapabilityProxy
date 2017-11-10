package org.cyclops.capabilityproxy.block;

import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;

/**
 * Config for {@link BlockCapabilityProxy}.
 * @author rubensworks
 */
public class BlockCapabilityProxyConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static BlockCapabilityProxyConfig _instance;

    /**
     * Make a new instance.
     */
    public BlockCapabilityProxyConfig() {
        super(
            CapabilityProxy._instance,
            true,
            "capability_proxy",
            null,
            BlockCapabilityProxy.class
        );
    }

    @Override
    public boolean isDisableable() {
        return true;
    }

}
