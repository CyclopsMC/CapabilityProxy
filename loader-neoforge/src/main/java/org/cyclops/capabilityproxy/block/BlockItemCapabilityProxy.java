package org.cyclops.capabilityproxy.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyNeoForge;
import org.cyclops.capabilityproxy.blockentity.BlockEntityItemCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockWithEntityGui;

import javax.annotation.Nullable;

/**
 * This block will forward capabilities from the contained item to all sides except for the target side.
 * @author rubensworks
 */
public class BlockItemCapabilityProxy extends BlockWithEntityGui {

    public static final MapCodec<BlockItemCapabilityProxy> CODEC = simpleCodec(BlockItemCapabilityProxy::new);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty INACTIVE = BooleanProperty.create("inactive");

    public BlockItemCapabilityProxy(Block.Properties properties) {
        super(properties, BlockEntityItemCapabilityProxy::new);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
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
                .setValue(INACTIVE, context.getLevel().getBlockEntity(BlockEntityCapabilityProxyNeoForge
                        .getTargetPos(context.getClickedPos(), context.getClickedFace().getOpposite())) == null);
    }

}
