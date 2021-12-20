package org.cyclops.capabilityproxy.block;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.cyclops.capabilityproxy.tileentity.TileCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockTile;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * This block will forward capabilities from the target side to all sides.
 * @author rubensworks
 */
public class BlockCapabilityProxy extends BlockTile {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty INACTIVE = BooleanProperty.create("inactive");

    private Set<BlockPos> activatingBlockChain = null;

    public BlockCapabilityProxy(Block.Properties properties) {
        super(properties, TileCapabilityProxy::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.DOWN)
                .setValue(INACTIVE, Boolean.valueOf(true)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING)
                .add(INACTIVE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace().getOpposite())
                .setValue(INACTIVE, context.getLevel().getBlockEntity(TileCapabilityProxy
                        .getTargetPos(context.getClickedPos(), context.getClickedFace().getOpposite())) == null);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
                                             BlockRayTraceResult hit) {
        // A check to avoid infinite loops
        if (activatingBlockChain == null) {
            activatingBlockChain = Sets.newHashSet(pos);
        } else {
            if (activatingBlockChain.contains(pos)) {
                return ActionResultType.FAIL;
            } else {
                activatingBlockChain.add(pos);
            }
        }
        Direction facing = state.getValue(BlockCapabilityProxy.FACING);
        BlockState targetBlockState = worldIn.getBlockState(pos.relative(facing));
        ActionResultType ret = targetBlockState.getBlock().use(targetBlockState, worldIn,
                TileCapabilityProxy.getTargetPos(pos, facing), player, handIn, hit.withDirection(facing.getOpposite()));
        activatingBlockChain = null;
        return ret;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
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
