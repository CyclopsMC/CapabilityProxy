package org.cyclops.capabilityproxy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockTile;

import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * This block will forward capabilities from the target's (at a range) side to all sides.
 * @author rubensworks
 */
public class BlockRangedCapabilityProxy extends BlockTile {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
                                             BlockRayTraceResult hit) {
        RecursiveHit rhit = hit instanceof RecursiveHit ? (RecursiveHit)hit : new RecursiveHit(hit, new HashSet<>(), hit.getPos(), hit.getFace());
        if (rhit.chain.contains(pos)) {
            rhit.setFailed();
            return ActionResultType.FAIL;
        }
        rhit.chain.add(pos.toImmutable());

        for (int offset = 1; offset < BlockRangedCapabilityProxyConfig.range; offset++) {
            Direction facing = state.get(BlockRangedCapabilityProxy.FACING);
            BlockPos targetPos = pos.offset(facing, offset);
            BlockState target = worldIn.getBlockState(targetPos);
            ActionResultType ret = target.onBlockActivated(worldIn, player, hand, rhit.move(targetPos, facing.getOpposite()));
            if (ret.isSuccessOrConsume() || rhit.failed) {
                return ret;
            }
        }
        return ActionResultType.PASS;
    }

    private static class RecursiveHit extends BlockRayTraceResult {
        private RecursiveHit rParent;
        private final BlockRayTraceResult parent;
        private final Set<BlockPos> chain;
        private boolean failed = false;

        public RecursiveHit(BlockRayTraceResult parent, Set<BlockPos> chain, BlockPos pos, Direction face) {
            super(parent.getHitVec(), face, pos, parent.isInside());
            this.parent = parent;
            this.chain = chain;
            this.hitInfo = parent.hitInfo;
        }

        private RecursiveHit(RecursiveHit parent, Direction face) {
            this(parent.parent, parent.chain, parent.getPos(), face);
            rParent = parent;
        }

        private RecursiveHit move(BlockPos pos, Direction face) {
            return new RecursiveHit(this.parent, this.chain, pos, face);
        }

        @Override
        public BlockRayTraceResult withFace(Direction newFace) {
            return new RecursiveHit(this, newFace);
        }

        @Override
        public Type getType() {
            return parent.getType();
        }

        @Override
        public Vec3d getHitVec() {
            return parent.getHitVec();
        }

        @Override
        public boolean isInside() {
            return parent.isInside();
        }

        public void setFailed() {
            if (rParent != null)
                rParent.setFailed();
            else
                this.failed = true;
        }
    }
}
