package org.cyclops.capabilityproxy;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxyConfig;
import org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxyConfig;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxyConfig;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxyConfig;
import org.cyclops.capabilityproxy.proxy.ClientProxy;
import org.cyclops.capabilityproxy.proxy.CommonProxy;
import org.cyclops.capabilityproxy.tileentity.TileCapabilityProxyConfig;
import org.cyclops.capabilityproxy.tileentity.TileEntityCapabilityProxyConfig;
import org.cyclops.capabilityproxy.tileentity.TileItemCapabilityProxyConfig;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxyConfig;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ItemGroupMod;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

/**
 * The main mod class of this mod.
 * @author rubensworks (aka kroeserr)
 *
 */
@Mod(Reference.MOD_ID)
public class CapabilityProxy extends ModBaseVersionable<CapabilityProxy> {

    /**
     * The unique instance of this mod.
     */
    public static CapabilityProxy _instance;

    public CapabilityProxy() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
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
    protected ItemGroup constructDefaultItemGroup() {
        return new ItemGroupMod(this, () -> RegistryEntries.ITEM_CAPABILITY_PROXY);
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig());

        configHandler.addConfigurable(new BlockCapabilityProxyConfig());
        configHandler.addConfigurable(new BlockEntityCapabilityProxyConfig());
        configHandler.addConfigurable(new BlockItemCapabilityProxyConfig());
        configHandler.addConfigurable(new BlockRangedCapabilityProxyConfig());

        configHandler.addConfigurable(new TileCapabilityProxyConfig());
        configHandler.addConfigurable(new TileEntityCapabilityProxyConfig());
        configHandler.addConfigurable(new TileItemCapabilityProxyConfig());
        configHandler.addConfigurable(new TileRangedCapabilityProxyConfig());

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
        CapabilityProxy._instance.getLoggerHelper().log(level, message);
    }
    
}
