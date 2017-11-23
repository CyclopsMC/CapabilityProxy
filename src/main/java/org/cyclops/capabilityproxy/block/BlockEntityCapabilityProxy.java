package org.cyclops.capabilityproxy.block;

import com.google.common.collect.Lists;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.capabilityproxy.tileentity.TileEntityCapabilityProxy;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * This block will forward capabilities from an entity at the target side to all sides.
 * @author josephcsible
 */
public class BlockEntityCapabilityProxy extends ConfigurableBlockContainer {

    @BlockProperty
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Lists.newArrayList(EnumFacing.VALUES));

    private static BlockEntityCapabilityProxy _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BlockEntityCapabilityProxy getInstance() {
        return _instance;
    }

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    public BlockEntityCapabilityProxy(ExtendedConfig eConfig) {
        super(eConfig, Material.GROUND, TileEntityCapabilityProxy.class);

        setHardness(2.0F);
        setSoundType(SoundType.STONE);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ,
                                            int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState()
                .withProperty(FACING, facing.getOpposite());
    }
}
