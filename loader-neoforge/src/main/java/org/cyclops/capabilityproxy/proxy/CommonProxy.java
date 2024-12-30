package org.cyclops.capabilityproxy.proxy;

import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBaseNeoForge<CapabilityProxyNeoForge> getMod() {
        return CapabilityProxyNeoForge._instance;
    }

}
