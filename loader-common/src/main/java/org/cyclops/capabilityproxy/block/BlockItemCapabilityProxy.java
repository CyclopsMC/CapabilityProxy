package org.cyclops.capabilityproxy.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyCommon;
import org.cyclops.cyclopscore.block.BlockWithEntityGuiCommon;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

/**
 * This block will forward capabilities from the contained item to all sides except for the target side.
 * @author rubensworks
 */
public class BlockItemCapabilityProxy extends BlockWithEntityGuiCommon {

    public final MapCodec<BlockItemCapabilityProxy> codec;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty INACTIVE = BooleanProperty.create("inactive");

    public BlockItemCapabilityProxy(Block.Properties properties, BiFunction<BlockPos, BlockState, ? extends CyclopsBlockEntityCommon> blockEntitySupplier) {
        super(properties, blockEntitySupplier);
        this.codec = BlockBehaviour.simpleCodec(p -> new BlockItemCapabilityProxy(p, blockEntitySupplier));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return codec;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING)
                .add(INACTIVE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace().getOpposite())
                .setValue(INACTIVE, context.getLevel().getBlockEntity(BlockEntityCapabilityProxyCommon
                        .getTargetPos(context.getClickedPos(), context.getClickedFace().getOpposite())) == null);
    }

}
