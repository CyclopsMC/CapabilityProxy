package org.cyclops.capabilityproxy.proxy;

import org.cyclops.capabilityproxy.CapabilityProxyForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxy;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.proxy.ClientProxyComponentForge;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxyForge extends ClientProxyComponentForge {

    public ClientProxyForge() {
        super(new CommonProxyForge());
    }

    @Override
    public ModBaseForge<?> getMod() {
        return CapabilityProxyForge._instance;
    }

    @Override
    public void registerRenderers() {
        this.registerRenderer(RegistryEntries.TILE_ENTITY_RANGED_CAPABILITY_PROXY.value(), RenderTileRangedCapabilityProxy::new);
        super.registerRenderers();
    }

}
