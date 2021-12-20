package org.cyclops.capabilityproxy.block;

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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.cyclops.capabilityproxy.blockentity.BlockEntityRangedCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockWithEntity;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * This block will forward capabilities from the target's (at a range) side to all sides.
 * @author rubensworks
 */
public class BlockRangedCapabilityProxy extends BlockWithEntity {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockRangedCapabilityProxy(Block.Properties properties) {
        super(properties, BlockEntityRangedCapabilityProxy::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand,
                                             BlockHitResult hit) {
        RecursiveHit rhit = hit instanceof RecursiveHit ? (RecursiveHit)hit : new RecursiveHit(hit, new HashSet<>(), hit.getBlockPos(), hit.getDirection());
        if (rhit.chain.contains(pos)) {
            rhit.setFailed();
            return InteractionResult.FAIL;
        }
        rhit.chain.add(pos.immutable());

        for (int offset = 1; offset < BlockRangedCapabilityProxyConfig.range; offset++) {
            Direction facing = state.getValue(BlockRangedCapabilityProxy.FACING);
            BlockPos targetPos = pos.relative(facing, offset);
            BlockState target = worldIn.getBlockState(targetPos);
            InteractionResult ret = target.use(worldIn, player, hand, rhit.move(targetPos, facing.getOpposite()));
            if (ret.consumesAction() || rhit.failed) {
                return ret;
            }
        }
        return InteractionResult.PASS;
    }

    private static class RecursiveHit extends BlockHitResult {
        private RecursiveHit rParent;
        private final BlockHitResult parent;
        private final Set<BlockPos> chain;
        private boolean failed = false;

        public RecursiveHit(BlockHitResult parent, Set<BlockPos> chain, BlockPos pos, Direction face) {
            super(parent.getLocation(), face, pos, parent.isInside());
            this.parent = parent;
            this.chain = chain;
        }

        private RecursiveHit(RecursiveHit parent, Direction face) {
            this(parent.parent, parent.chain, parent.getBlockPos(), face);
            rParent = parent;
        }

        private RecursiveHit move(BlockPos pos, Direction face) {
            return new RecursiveHit(this.parent, this.chain, pos, face);
        }

        @Override
        public BlockHitResult withDirection(Direction newFace) {
            return new RecursiveHit(this, newFace);
        }

        @Override
        public Type getType() {
            return parent.getType();
        }

        @Override
        public Vec3 getLocation() {
            return parent.getLocation();
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
