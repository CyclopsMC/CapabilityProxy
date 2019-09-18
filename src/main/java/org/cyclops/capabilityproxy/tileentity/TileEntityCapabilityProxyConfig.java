package org.cyclops.capabilityproxy.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

/**
 * Config for the {@link TileEntityCapabilityProxy}.
 * @author rubensworks
 *
 */
public class TileEntityCapabilityProxyConfig extends TileEntityConfig<TileEntityCapabilityProxy> {

    public TileEntityCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "entity_capability_proxy",
                (eConfig) -> new TileEntityType<>(TileEntityCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY), null)
        );
    }

}
