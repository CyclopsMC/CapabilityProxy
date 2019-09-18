package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.client.gui.ContainerScreenItemCapabilityProxy;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;

/**
 * Config for {@link ContainerItemCapabilityProxy}.
 * @author rubensworks
 */
public class ContainerItemCapabilityProxyConfig extends GuiConfig<ContainerItemCapabilityProxy> {

    public ContainerItemCapabilityProxyConfig() {
        super(CapabilityProxy._instance,
                "item_capability_proxy",
                eConfig -> new ContainerType<>(ContainerItemCapabilityProxy::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerItemCapabilityProxy>> ScreenManager.IScreenFactory<ContainerItemCapabilityProxy, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenItemCapabilityProxy::new);
    }

}
