package org.cyclops.capabilityproxy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.BiFunction;

/**
 * Config for {@link BlockRangedCapabilityProxy}.
 * @author rubensworks
 */
public class BlockRangedCapabilityProxyConfig<M extends IModBase> extends BlockConfigCommon<M> {

    @ConfigurablePropertyCommon(category = "block", comment = "The maximum range in number of blocks. Warning: high values can lag and/or crash your game.", minimalValue = 1, configLocation = ModConfigLocation.SERVER)
    public static int range = 16;

    public BlockRangedCapabilityProxyConfig(M mod, BiFunction<BlockPos, BlockState, ? extends CyclopsBlockEntityCommon> blockEntitySupplier) {
        super(
                mod,
                "ranged_capability_proxy",
                (eConfig) -> new BlockRangedCapabilityProxy(Block.Properties.of()
                        .sound(SoundType.STONE)
                        .strength(2.0f), blockEntitySupplier),
                BlockConfigCommon.getDefaultItemConstructor(mod)
        );
    }

}
