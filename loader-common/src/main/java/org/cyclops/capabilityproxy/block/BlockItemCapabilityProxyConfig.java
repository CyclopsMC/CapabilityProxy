package org.cyclops.capabilityproxy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.BiFunction;

/**
 * Config for {@link BlockCapabilityProxy}.
 * @author rubensworks
 */
public class BlockItemCapabilityProxyConfig<M extends IModBase> extends BlockConfigCommon<M> {

    public BlockItemCapabilityProxyConfig(M mod, BiFunction<BlockPos, BlockState, ? extends CyclopsBlockEntity> blockEntitySupplier) {
        super(
                mod,
                "item_capability_proxy",
                (eConfig, properties) -> new BlockItemCapabilityProxy(properties
                        .sound(SoundType.STONE)
                        .strength(2.0f), blockEntitySupplier),
                BlockConfigCommon.getDefaultItemConstructor(mod)
        );
    }

}
