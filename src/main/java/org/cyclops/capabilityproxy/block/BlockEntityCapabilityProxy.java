package org.cyclops.capabilityproxy.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.cyclops.capabilityproxy.blockentity.BlockEntityEntityCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockWithEntity;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This block will forward capabilities from an entity at the target side to all sides.
 * @author josephcsible
 */
public class BlockEntityCapabilityProxy extends BlockWithEntity {

    public static final MapCodec<BlockEntityCapabilityProxy> CODEC = simpleCodec(BlockEntityCapabilityProxy::new);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockEntityCapabilityProxy(Block.Properties properties) {
        super(properties, BlockEntityEntityCapabilityProxy::new);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
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
        Direction facing = state.getValue(FACING);
        if(hit.getDirection() == facing) return InteractionResult.SUCCESS; // In the future, this will be how you open the filter GUI
        BlockPos targetPos = pos.relative(facing);
        List<Entity> entities = worldIn.getEntitiesOfClass(Entity.class, new AABB(targetPos));
        for(Entity entity : entities) {
            InteractionResult result = entity.interactAt(player, new Vec3(targetPos.getX() + 0.5 - entity.getX(), targetPos.getY() + 0.5 - entity.getY(), targetPos.getZ() + 0.5 - entity.getZ()), hand);
            if(result != InteractionResult.PASS)
                return result;
            result = player.interactOn(entity, hand);
            if(result != InteractionResult.PASS)
                return result;
        }
        return InteractionResult.PASS;
    }
}
