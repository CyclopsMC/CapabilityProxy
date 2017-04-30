package org.cyclops.capabilityproxy.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.capabilityproxy.tileentity.TileCapabilityProxy;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * This block will forward capabilities from the target side to all sides.
 * @author rubensworks
 */
public class BlockCapabilityProxy extends ConfigurableBlockContainer {

    @BlockProperty
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Lists.newArrayList(EnumFacing.VALUES));
    @BlockProperty
    public static final PropertyBool INACTIVE = PropertyBool.create("inactive");

    private static BlockCapabilityProxy _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BlockCapabilityProxy getInstance() {
        return _instance;
    }

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    public BlockCapabilityProxy(ExtendedConfig eConfig) {
        super(eConfig, Material.GROUND, TileCapabilityProxy.class);

        setHardness(2.0F);
        setSoundType(SoundType.STONE);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta,
                                            EntityLivingBase placer, ItemStack stack) {
        return this.getDefaultState()
                .withProperty(FACING, facing.getOpposite())
                .withProperty(INACTIVE, world.getTileEntity(pos.offset(facing.getOpposite())) == null);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, ItemStack heldItem, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        EnumFacing facing = state.getValue(BlockCapabilityProxy.FACING);
        boolean inactive = state.getValue(BlockCapabilityProxy.INACTIVE);
        if (!inactive) {
            IBlockState targetBlockState = worldIn.getBlockState(pos.offset(facing));
            return targetBlockState.getBlock().onBlockActivated(worldIn, pos.offset(facing), targetBlockState,
                    playerIn, hand, heldItem, facing, hitX, hitY, hitZ);
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        super.neighborChanged(state, world, pos, block);
        if (!world.isRemote) {
            EnumFacing facing = state.getValue(BlockCapabilityProxy.FACING);
            boolean inactive = state.getValue(BlockCapabilityProxy.INACTIVE);
            if (inactive != (world.getTileEntity(pos.offset(facing)) == null)) {
                world.setBlockState(pos, world.getBlockState(pos).withProperty(INACTIVE, !inactive));
                world.notifyNeighborsOfStateExcept(pos, this, facing);
            }
        }
    }
}
