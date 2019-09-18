package org.cyclops.capabilityproxy.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

/**
 * Config for the {@link TileRangedCapabilityProxy}.
 * @author rubensworks
 *
 */
public class TileRangedCapabilityProxyConfig extends TileEntityConfig<TileRangedCapabilityProxy> {

    public TileRangedCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "ranged_capability_proxy",
                (eConfig) -> new TileEntityType<>(TileRangedCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY), null)
        );
    }

}
