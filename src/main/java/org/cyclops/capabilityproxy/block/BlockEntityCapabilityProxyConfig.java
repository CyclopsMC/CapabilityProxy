package org.cyclops.capabilityproxy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for {@link BlockEntityCapabilityProxy}.
 * @author josephcsible
 */
public class BlockEntityCapabilityProxyConfig extends BlockConfig {

    public BlockEntityCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "entity_capability_proxy",
                (eConfig) -> new BlockEntityCapabilityProxy(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(2.0f)),
                getDefaultItemConstructor(CapabilityProxy._instance)
        );
    }

}
