package org.cyclops.capabilityproxy.block;

import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyCommon;
import org.cyclops.cyclopscore.block.BlockWithEntityCommon;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * This block will forward capabilities from the target side to all sides.
 * @author rubensworks
 */
public class BlockCapabilityProxy extends BlockWithEntityCommon {

    public final MapCodec<BlockCapabilityProxy> codec;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty INACTIVE = BooleanProperty.create("inactive");

    private Set<BlockPos> activatingBlockChain = null;

    public BlockCapabilityProxy(Block.Properties properties, BiFunction<BlockPos, BlockState, ? extends CyclopsBlockEntityCommon> blockEntitySupplier) {
        super(properties, blockEntitySupplier);

        this.codec = BlockBehaviour.simpleCodec(p -> new BlockCapabilityProxy(p, blockEntitySupplier));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.DOWN)
                .setValue(INACTIVE, Boolean.valueOf(true)));
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

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        // A check to avoid infinite loops
        if (activatingBlockChain == null) {
            activatingBlockChain = Sets.newHashSet(pos);
        } else {
            if (activatingBlockChain.contains(pos)) {
                return InteractionResult.FAIL;
            } else {
                activatingBlockChain.add(pos);
            }
        }
        Direction facing = blockState.getValue(BlockCapabilityProxy.FACING);
        BlockState targetBlockState = level.getBlockState(pos.relative(facing));
        InteractionResult ret = targetBlockState.useWithoutItem(level, player, hit
                .withDirection(facing.getOpposite())
                .withPosition(BlockEntityCapabilityProxyCommon.getTargetPos(pos, facing)));
        activatingBlockChain = null;
        return ret;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        if (!world.isClientSide) {
            Direction facing = state.getValue(BlockCapabilityProxy.FACING);
            if (pos.relative(facing).equals(fromPos)) {
                boolean inactive = state.getValue(BlockCapabilityProxy.INACTIVE);
                if (inactive != (world.getBlockEntity(pos.relative(facing)) == null)) {
                    world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(INACTIVE, !inactive));
                    world.updateNeighborsAtExceptFromFacing(pos, this, facing);
                }
            }
        }
    }

}
