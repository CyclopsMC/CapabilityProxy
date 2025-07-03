package org.cyclops.capabilityproxy.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeDataCommon;

/**
 * Config for {@link ContainerItemCapabilityProxy}.
 * @author rubensworks
 */
public class ContainerItemCapabilityProxyConfig<M extends IModBase> extends GuiConfigCommon<ContainerItemCapabilityProxy, M> {

    public ContainerItemCapabilityProxyConfig(M mod) {
        super(mod,
                "item_capability_proxy",
                eConfig -> new ContainerTypeDataCommon<>(ContainerItemCapabilityProxy::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerItemCapabilityProxy> getScreenFactoryProvider() {
        return new ContainerItemCapabilityProxyConfigScreenFactoryProvider();
    }
}
