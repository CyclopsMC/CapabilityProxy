package org.cyclops.capabilityproxy.block;

import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;

/**
 * Config for {@link BlockRangedCapabilityProxy}.
 * @author rubensworks
 */
public class BlockRangedCapabilityProxyConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static BlockRangedCapabilityProxyConfig _instance;

    /**
     * The maximum range in number of blocks.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "The maximum range in number of blocks.", minimalValue = 1)
    public static int range = 16;

    /**
     * Make a new instance.
     */
    public BlockRangedCapabilityProxyConfig() {
        super(
            CapabilityProxy._instance,
            true,
            "ranged_capability_proxy",
            null,
            BlockRangedCapabilityProxy.class
        );
    }

    @Override
    public boolean isDisableable() {
        return true;
    }

}
