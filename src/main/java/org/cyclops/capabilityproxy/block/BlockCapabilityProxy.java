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

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.DOWN)
                .with(INACTIVE, Boolean.valueOf(true)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING)
                .add(INACTIVE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState()
                .with(FACING, context.getFace().getOpposite())
                .with(INACTIVE, context.getWorld().getTileEntity(TileCapabilityProxy
                        .getTargetPos(context.getPos(), context.getFace().getOpposite())) == null);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
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
        Direction facing = state.get(BlockCapabilityProxy.FACING);
        BlockState targetBlockState = worldIn.getBlockState(pos.offset(facing));
        ActionResultType ret = targetBlockState.getBlock().onBlockActivated(targetBlockState, worldIn,
                TileCapabilityProxy.getTargetPos(pos, facing), player, handIn, hit.withFace(facing.getOpposite()));
        activatingBlockChain = null;
        return ret;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        if (!world.isRemote) {
            Direction facing = state.get(BlockCapabilityProxy.FACING);
            if (pos.offset(facing).equals(fromPos)) {
                boolean inactive = state.get(BlockCapabilityProxy.INACTIVE);
                if (inactive != (world.getTileEntity(pos.offset(facing)) == null)) {
                    world.setBlockState(pos, world.getBlockState(pos).with(INACTIVE, !inactive));
                    world.notifyNeighborsOfStateExcept(pos, this, facing);
                }
            }
        }
    }

}
