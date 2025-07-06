package org.cyclops.capabilityproxy.proxy;

import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxyNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxy extends ClientProxyComponent {

    public ClientProxy() {
        super(new CommonProxy());
    }

    @Override
    public ModBaseNeoForge<CapabilityProxyNeoForge> getMod() {
        return CapabilityProxyNeoForge._instance;
    }

    @Override
    public void registerRenderers() {
        this.registerRenderer(RegistryEntries.TILE_ENTITY_RANGED_CAPABILITY_PROXY.value(), RenderTileRangedCapabilityProxyNeoForge::new);
        super.registerRenderers();
    }
}
