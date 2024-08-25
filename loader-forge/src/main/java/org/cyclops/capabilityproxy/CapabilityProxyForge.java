package org.cyclops.capabilityproxy;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyForge;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyForgeConfig;
import org.cyclops.capabilityproxy.proxy.ClientProxyForge;
import org.cyclops.capabilityproxy.proxy.CommonProxyForge;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;

/**
 * The main mod class of this mod.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class CapabilityProxyForge extends ModBaseForge<CapabilityProxyForge> {

    /**
     * The unique instance of this mod.
     */
    public static CapabilityProxyForge _instance;

    public CapabilityProxyForge() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyForge();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyForge();
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

        configHandler.addConfigurable(new BlockCapabilityProxyConfig<>(this, BlockEntityCapabilityProxyForge::new));

        configHandler.addConfigurable(new BlockEntityCapabilityProxyForgeConfig());
    }
}
