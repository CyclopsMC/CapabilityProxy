package org.cyclops.capabilityproxy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for {@link BlockCapabilityProxy}.
 * @author rubensworks
 */
public class BlockItemCapabilityProxyConfig extends BlockConfig {

    public BlockItemCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "item_capability_proxy",
                (eConfig) -> new BlockItemCapabilityProxy(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(2.0f)),
                getDefaultItemConstructor(CapabilityProxy._instance)
        );
    }

}
