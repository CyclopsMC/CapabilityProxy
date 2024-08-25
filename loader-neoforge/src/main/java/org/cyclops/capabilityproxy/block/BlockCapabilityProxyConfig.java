package org.cyclops.capabilityproxy.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.capabilityproxy.CapabilityProxyNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for {@link BlockCapabilityProxy}.
 * @author rubensworks
 */
public class BlockCapabilityProxyConfig extends BlockConfig {

    public BlockCapabilityProxyConfig() {
        super(
                CapabilityProxyNeoForge._instance,
                "capability_proxy",
                (eConfig) -> new BlockCapabilityProxy(Block.Properties.of()
                        .sound(SoundType.STONE)
                        .strength(2.0f)),
                getDefaultItemConstructor(CapabilityProxyNeoForge._instance)
        );
    }

}
