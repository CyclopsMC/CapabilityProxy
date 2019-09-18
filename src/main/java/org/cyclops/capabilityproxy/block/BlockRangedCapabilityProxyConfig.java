package org.cyclops.capabilityproxy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Config for {@link BlockRangedCapabilityProxy}.
 * @author rubensworks
 */
public class BlockRangedCapabilityProxyConfig extends BlockConfig {

    @ConfigurableProperty(category = "block", comment = "The maximum range in number of blocks. Warning: high values can lag and/or crash your game.", minimalValue = 1, configLocation = ModConfig.Type.SERVER)
    public static int range = 16;

    public BlockRangedCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "ranged_capability_proxy",
                (eConfig) -> new BlockRangedCapabilityProxy(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(2.0f)),
                getDefaultItemConstructor(CapabilityProxy._instance)
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if(MinecraftHelpers.isClientSide()) {
            registerClientSide();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void registerClientSide() {
        CapabilityProxy._instance.getProxy().registerRenderer(TileRangedCapabilityProxy.class, new RenderTileRangedCapabilityProxy());
    }

}
