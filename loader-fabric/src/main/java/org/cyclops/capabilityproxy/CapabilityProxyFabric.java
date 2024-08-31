package org.cyclops.capabilityproxy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyFabric;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyFabricConfig;
import org.cyclops.capabilityproxy.proxy.ClientProxyFabric;
import org.cyclops.capabilityproxy.proxy.CommonProxyFabric;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;

/**
 * The main mod class of CapabilityProxy.
 * @author rubensworks
 */
public class CapabilityProxyFabric extends ModBaseFabric<CapabilityProxyFabric> implements ModInitializer {

    /**
     * The unique instance of this mod.
     */
    public static CapabilityProxyFabric _instance;

    public CapabilityProxyFabric() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyFabric();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyFabric();
    }

    @Override
    protected boolean hasDefaultCreativeModeTab() {
        return true;
    }

    @Override
    protected CreativeModeTab.Builder constructDefaultCreativeModeTab(CreativeModeTab.Builder builder) {
        return super.constructDefaultCreativeModeTab(builder)
                .icon(() -> new ItemStack(RegistryEntries.ITEM_CAPABILITY_PROXY));
    }

    @Override
    protected void onConfigsRegister(ConfigHandlerCommon configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));

        configHandler.addConfigurable(new BlockCapabilityProxyConfig<>(this, BlockEntityCapabilityProxyFabric::new));

        configHandler.addConfigurable(new BlockEntityCapabilityProxyFabricConfig());
    }
}
