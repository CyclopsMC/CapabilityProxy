package org.cyclops.capabilityproxy.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

/**
 * Config for the {@link TileCapabilityProxy}.
 * @author rubensworks
 *
 */
public class TileCapabilityProxyConfig extends TileEntityConfig<TileCapabilityProxy> {

    public TileCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "capability_proxy",
                (eConfig) -> new TileEntityType<>(TileCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_CAPABILITY_PROXY), null)
        );
    }

}
