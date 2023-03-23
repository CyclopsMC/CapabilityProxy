package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
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
                eConfig -> new MenuType<>(ContainerItemCapabilityProxy::new, FeatureFlags.VANILLA_SET));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & MenuAccess<ContainerItemCapabilityProxy>> MenuScreens.ScreenConstructor<ContainerItemCapabilityProxy, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenItemCapabilityProxy::new);
    }

}
