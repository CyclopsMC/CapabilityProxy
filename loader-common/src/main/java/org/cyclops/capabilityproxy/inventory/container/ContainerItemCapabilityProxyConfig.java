package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for {@link ContainerItemCapabilityProxy}.
 * @author rubensworks
 */
public class ContainerItemCapabilityProxyConfig<M extends IModBase> extends GuiConfigCommon<ContainerItemCapabilityProxy, M> {

    public ContainerItemCapabilityProxyConfig(M mod) {
        super(mod,
                "item_capability_proxy",
                eConfig -> new MenuType<>(ContainerItemCapabilityProxy::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerItemCapabilityProxy> getScreenFactoryProvider() {
        return new ContainerItemCapabilityProxyConfigScreenFactoryProvider();
    }
}
