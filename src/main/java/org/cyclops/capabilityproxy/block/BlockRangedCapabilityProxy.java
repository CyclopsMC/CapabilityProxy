package org.cyclops.capabilityproxy.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.capabilityproxy.tileentity.TileRangedCapabilityProxy;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.Set;

/**
 * This block will forward capabilities from the target's (at a range) side to all sides.
 * @author rubensworks
 */
public class BlockRangedCapabilityProxy extends ConfigurableBlockContainer {

    @BlockProperty
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Lists.newArrayList(EnumFacing.VALUES));

    private static BlockRangedCapabilityProxy _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BlockRangedCapabilityProxy getInstance() {
        return _instance;
    }

    private Set<BlockPos> activatingBlockChain = Sets.newHashSet();

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    public BlockRangedCapabilityProxy(ExtendedConfig eConfig) {
        super(eConfig, Material.GROUND, TileRangedCapabilityProxy.class);

        setHardness(2.0F);
        setSoundType(SoundType.STONE);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ,
                                            int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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
            EnumFacing facing = state.getValue(BlockRangedCapabilityProxy.FACING);
            IBlockState targetBlockState = worldIn.getBlockState(pos.offset(facing, offset));
            boolean ret = targetBlockState.getBlock().onBlockActivated(worldIn, pos.offset(facing, offset), targetBlockState,
                    playerIn, hand, facing.getOpposite(), hitX, hitY, hitZ);
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
