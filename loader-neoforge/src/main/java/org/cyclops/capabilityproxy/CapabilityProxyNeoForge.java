package org.cyclops.capabilityproxy;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxyConfig;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxyConfig;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyNeoForge;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyNeoForgeConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityEntityCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityItemCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxyConfig;
import org.cyclops.capabilityproxy.proxy.ClientProxy;
import org.cyclops.capabilityproxy.proxy.CommonProxy;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

/**
 * The main mod class of this mod.
 * @author rubensworks (aka kroeserr)
 *
 */
@Mod(Reference.MOD_ID)
public class CapabilityProxyNeoForge extends ModBaseVersionable<CapabilityProxyNeoForge> {

    /**
     * The unique instance of this mod.
     */
    public static CapabilityProxyNeoForge _instance;

    public CapabilityProxyNeoForge(IEventBus modEventBus) {
        super(Reference.MOD_ID, (instance) -> _instance = instance, modEventBus);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
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

        configHandler.addConfigurable(new BlockCapabilityProxyConfig<>(this, BlockEntityCapabilityProxyNeoForge::new));
        configHandler.addConfigurable(new org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxyConfig());
        configHandler.addConfigurable(new BlockItemCapabilityProxyConfig());
        configHandler.addConfigurable(new BlockRangedCapabilityProxyConfig());

        configHandler.addConfigurable(new BlockEntityCapabilityProxyNeoForgeConfig());
        configHandler.addConfigurable(new BlockEntityEntityCapabilityProxyConfig());
        configHandler.addConfigurable(new BlockEntityItemCapabilityProxyConfig());
        configHandler.addConfigurable(new BlockEntityRangedCapabilityProxyConfig());

        configHandler.addConfigurable(new ContainerItemCapabilityProxyConfig());
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void clog(String message) {
        clog(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public static void clog(Level level, String message) {
        CapabilityProxyNeoForge._instance.getLoggerHelper().log(level, message);
    }

}
