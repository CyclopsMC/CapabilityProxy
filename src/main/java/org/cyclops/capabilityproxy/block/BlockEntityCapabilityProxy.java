package org.cyclops.capabilityproxy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.cyclops.capabilityproxy.tileentity.TileEntityCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockTile;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This block will forward capabilities from an entity at the target side to all sides.
 * @author josephcsible
 */
public class BlockEntityCapabilityProxy extends BlockTile {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockEntityCapabilityProxy(Block.Properties properties) {
        super(properties, TileEntityCapabilityProxy::new);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
                                    BlockRayTraceResult hit) {
        Direction facing = state.getValue(FACING);
        if(hit.getDirection() == facing) return ActionResultType.SUCCESS; // In the future, this will be how you open the filter GUI
        BlockPos targetPos = pos.relative(facing);
        List<Entity> entities = worldIn.getEntitiesOfClass(Entity.class, new AxisAlignedBB(targetPos));
        for(Entity entity : entities) {
            ActionResultType result = entity.interactAt(player, new Vector3d(targetPos.getX() + 0.5 - entity.getX(), targetPos.getY() + 0.5 - entity.getY(), targetPos.getZ() + 0.5 - entity.getZ()), hand);
            if(result != ActionResultType.PASS)
                return result;
            result = player.interactOn(entity, hand);
            if(result != ActionResultType.PASS)
                return result;
        }
        return ActionResultType.PASS;
    }
}
