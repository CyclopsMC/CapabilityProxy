package org.cyclops.capabilityproxy.block;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockTile;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * This block will forward capabilities from the target's (at a range) side to all sides.
 * @author rubensworks
 */
public class BlockRangedCapabilityProxy extends BlockTile {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private Set<BlockPos> activatingBlockChain = Sets.newHashSet();

    public BlockRangedCapabilityProxy(Block.Properties properties) {
        super(properties, TileRangedCapabilityProxy::new);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState()
                .with(FACING, context.getFace().getOpposite());
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
                                    BlockRayTraceResult hit) {
        // A check to avoid infinite loops
        if (activatingBlockChain == null) {
            activatingBlockChain = Sets.newHashSet(pos);
        } else {
            if (activatingBlockChain.contains(pos)) {
                return false;
            } else {
                activatingBlockChain.add(pos);
            }
        }
        for (int offset = 1; offset < BlockRangedCapabilityProxyConfig.range; offset++) {
            Direction facing = state.get(BlockRangedCapabilityProxy.FACING);
            BlockState targetBlockState = worldIn.getBlockState(pos.offset(facing, offset));
            boolean ret = targetBlockState.getBlock().onBlockActivated(targetBlockState, worldIn, pos.offset(facing, offset),
                    player, hand, hit.withFace(facing.getOpposite()));
            // TODO: this can produce a stackoverflow when in cycle, that is because we have this offsetloop here. FIXME
            if (ret) {
                activatingBlockChain = null;
                return ret;
            }
        }
        activatingBlockChain = null;
        return false;
    }
}
