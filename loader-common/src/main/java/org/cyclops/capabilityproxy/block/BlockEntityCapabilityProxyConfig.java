package org.cyclops.capabilityproxy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.BiFunction;

/**
 * Config for {@link BlockEntityCapabilityProxy}.
 * @author josephcsible
 */
public class BlockEntityCapabilityProxyConfig<M extends IModBase> extends BlockConfigCommon<M> {

    public BlockEntityCapabilityProxyConfig(M mod, BiFunction<BlockPos, BlockState, ? extends CyclopsBlockEntityCommon> blockEntitySupplier) {
        super(
                mod,
                "entity_capability_proxy",
                (eConfig) -> new BlockEntityCapabilityProxy(Block.Properties.of()
                        .sound(SoundType.STONE)
                        .strength(2.0f), blockEntitySupplier),
                BlockConfigCommon.getDefaultItemConstructor(mod)
        );
    }

}
