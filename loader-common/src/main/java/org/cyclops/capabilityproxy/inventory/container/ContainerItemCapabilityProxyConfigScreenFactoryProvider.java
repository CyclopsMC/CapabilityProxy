package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.capabilityproxy.client.gui.ContainerScreenItemCapabilityProxy;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;

/**
 * @author rubensworks
 */
public class ContainerItemCapabilityProxyConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerItemCapabilityProxy>  {
    @Override
    public <U extends Screen & MenuAccess<ContainerItemCapabilityProxy>> MenuScreens.ScreenConstructor<ContainerItemCapabilityProxy, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenItemCapabilityProxy::new);
    }
}
