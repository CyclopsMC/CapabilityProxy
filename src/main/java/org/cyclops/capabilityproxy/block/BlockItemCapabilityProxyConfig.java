package org.cyclops.capabilityproxy.block;

import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;

/**
 * Config for {@link BlockCapabilityProxy}.
 * @author rubensworks
 */
public class BlockItemCapabilityProxyConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static BlockItemCapabilityProxyConfig _instance;

    /**
     * Make a new instance.
     */
    public BlockItemCapabilityProxyConfig() {
        super(
            CapabilityProxy._instance,
            true,
            "item_capability_proxy",
            null,
            BlockItemCapabilityProxy.class
        );
    }

    @Override
    public boolean isDisableable() {
        return true;
    }

}
