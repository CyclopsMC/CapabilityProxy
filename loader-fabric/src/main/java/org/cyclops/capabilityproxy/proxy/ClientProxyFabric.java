package org.cyclops.capabilityproxy.proxy;

import org.cyclops.capabilityproxy.CapabilityProxyFabric;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxy;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.ClientProxyComponentFabric;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxyFabric extends ClientProxyComponentFabric {

    public ClientProxyFabric() {
        super(new CommonProxyFabric());
    }

    @Override
    public ModBaseFabric<?> getMod() {
        return CapabilityProxyFabric._instance;
    }

    @Override
    public void registerRenderers() {
        this.registerRenderer(RegistryEntries.TILE_ENTITY_RANGED_CAPABILITY_PROXY.value(), RenderTileRangedCapabilityProxy::new);

        super.registerRenderers();
    }
}
