package org.cyclops.capabilityproxy.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.cyclops.cyclopscore.block.BlockWithEntityCommon;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;

/**
 * This block will forward capabilities from an entity at the target side to all sides.
 * @author josephcsible
 */
public class BlockEntityCapabilityProxy extends BlockWithEntityCommon {

    public final MapCodec<BlockEntityCapabilityProxy> codec;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockEntityCapabilityProxy(Block.Properties properties, BiFunction<BlockPos, BlockState, ? extends CyclopsBlockEntityCommon> blockEntitySupplier) {
        super(properties, blockEntitySupplier);
        this.codec = BlockBehaviour.simpleCodec(p -> new BlockEntityCapabilityProxy(p, blockEntitySupplier));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return codec;
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
    public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hit) {
        Direction facing = state.getValue(FACING);
        if(hit.getDirection() == facing) return InteractionResult.SUCCESS; // In the future, this will be how you open the filter GUI
        BlockPos targetPos = pos.relative(facing);
        List<Entity> entities = worldIn.getEntitiesOfClass(Entity.class, new AABB(targetPos));
        for(Entity entity : entities) {
            InteractionResult result = entity.interactAt(player, new Vec3(targetPos.getX() + 0.5 - entity.getX(), targetPos.getY() + 0.5 - entity.getY(), targetPos.getZ() + 0.5 - entity.getZ()), player.getUsedItemHand());
            if(result != InteractionResult.PASS)
                return result;
            result = player.interactOn(entity, player.getUsedItemHand());
            if(result != InteractionResult.PASS)
                return result;
        }
        return InteractionResult.PASS;
    }
}
