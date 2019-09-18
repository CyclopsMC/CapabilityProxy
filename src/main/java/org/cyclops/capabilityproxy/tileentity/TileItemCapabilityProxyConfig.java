package org.cyclops.capabilityproxy.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

/**
 * Config for the {@link TileItemCapabilityProxy}.
 * @author rubensworks
 *
 */
public class TileItemCapabilityProxyConfig extends TileEntityConfig<TileItemCapabilityProxy> {

    public TileItemCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "item_capability_proxy",
                (eConfig) -> new TileEntityType<>(TileItemCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY), null)
        );
    }

}
