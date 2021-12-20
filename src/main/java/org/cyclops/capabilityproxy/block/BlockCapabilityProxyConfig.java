package org.cyclops.capabilityproxy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for {@link BlockCapabilityProxy}.
 * @author rubensworks
 */
public class BlockCapabilityProxyConfig extends BlockConfig {

    public BlockCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "capability_proxy",
                (eConfig) -> new BlockCapabilityProxy(Block.Properties.of(Material.STONE)
                        .strength(2.0f)),
                getDefaultItemConstructor(CapabilityProxy._instance)
        );
    }

}
