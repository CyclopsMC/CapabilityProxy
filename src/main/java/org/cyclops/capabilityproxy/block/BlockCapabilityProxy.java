package org.cyclops.capabilityproxy.block;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockWithEntity;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * This block will forward capabilities from the target side to all sides.
 * @author rubensworks
 */
public class BlockCapabilityProxy extends BlockWithEntity {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty INACTIVE = BooleanProperty.create("inactive");

    private Set<BlockPos> activatingBlockChain = null;

    public BlockCapabilityProxy(Block.Properties properties) {
        super(properties, BlockEntityCapabilityProxy::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.DOWN)
                .setValue(INACTIVE, Boolean.valueOf(true)));
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
                .setValue(INACTIVE, context.getLevel().getBlockEntity(BlockEntityCapabilityProxy
                        .getTargetPos(context.getClickedPos(), context.getClickedFace().getOpposite())) == null);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                             BlockHitResult hit) {
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
        Direction facing = state.getValue(BlockCapabilityProxy.FACING);
        BlockState targetBlockState = worldIn.getBlockState(pos.relative(facing));
        InteractionResult ret = targetBlockState.getBlock().use(targetBlockState, worldIn,
                BlockEntityCapabilityProxy.getTargetPos(pos, facing), player, handIn, hit.withDirection(facing.getOpposite()));
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
