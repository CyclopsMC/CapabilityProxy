package org.cyclops.capabilityproxy.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Config for {@link BlockRangedCapabilityProxy}.
 * @author rubensworks
 */
public class BlockRangedCapabilityProxyConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static BlockRangedCapabilityProxyConfig _instance;

    /**
     * The maximum range in number of blocks.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.BLOCK, comment = "The maximum range in number of blocks. Warning: high values can lag and/or crash your game.", minimalValue = 1)
    public static int range = 16;

    /**
     * Make a new instance.
     */
    public BlockRangedCapabilityProxyConfig() {
        super(
            CapabilityProxy._instance,
            true,
            "ranged_capability_proxy",
            null,
            BlockRangedCapabilityProxy.class
        );
    }

    @Override
    public boolean isDisableable() {
        return true;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if(MinecraftHelpers.isClientSide()) {
            registerClientSide();
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerClientSide() {
        CapabilityProxy.proxy.registerRenderer(TileRangedCapabilityProxy.class, new RenderTileRangedCapabilityProxy());
    }

}
