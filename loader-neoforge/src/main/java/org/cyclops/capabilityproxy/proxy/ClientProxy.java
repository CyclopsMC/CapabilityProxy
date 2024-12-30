package org.cyclops.capabilityproxy.proxy;

import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
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

}
