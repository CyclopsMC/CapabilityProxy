package org.cyclops.capabilityproxy.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for {@link BlockEntityCapabilityProxy}.
 * @author josephcsible
 */
public class BlockEntityCapabilityProxyConfig extends BlockConfig {

    public BlockEntityCapabilityProxyConfig() {
        super(
                CapabilityProxyNeoForge._instance,
                "entity_capability_proxy",
                (eConfig) -> new BlockEntityCapabilityProxy(Block.Properties.of()
                        .sound(SoundType.STONE)
                        .strength(2.0f)),
                getDefaultItemConstructor(CapabilityProxyNeoForge._instance)
        );
    }

}
