package org.cyclops.capabilityproxy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import org.cyclops.capabilityproxy.tileentity.TileCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileItemCapabilityProxy;
import org.cyclops.cyclopscore.block.BlockTileGui;

import javax.annotation.Nullable;

/**
 * This block will forward capabilities from the contained item to all sides except for the target side.
 * @author rubensworks
 */
public class BlockItemCapabilityProxy extends BlockTileGui {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty INACTIVE = BooleanProperty.create("inactive");

    public BlockItemCapabilityProxy(Block.Properties properties) {
        super(properties, TileItemCapabilityProxy::new);
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

}
