package org.cyclops.capabilityproxy.block;

import com.google.common.collect.Lists;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.capabilityproxy.client.gui.GuiItemCapabilityProxy;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxy;
import org.cyclops.capabilityproxy.tileentity.TileItemCapabilityProxy;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainerGui;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * This block will forward capabilities from the contained item to all sides except for the target side.
 * @author rubensworks
 */
public class BlockItemCapabilityProxy extends ConfigurableBlockContainerGui {

    @BlockProperty
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Lists.newArrayList(EnumFacing.VALUES));
    @BlockProperty
    public static final PropertyBool INACTIVE = PropertyBool.create("inactive");

    private static BlockItemCapabilityProxy _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BlockItemCapabilityProxy getInstance() {
        return _instance;
    }

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    public BlockItemCapabilityProxy(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.GROUND, TileItemCapabilityProxy.class);

        setHardness(2.0F);
        setSoundType(SoundType.STONE);
        this.hasGui = true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ,
                                            int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState()
                .withProperty(FACING, facing.getOpposite())
                .withProperty(INACTIVE, false);
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerItemCapabilityProxy.class;
    }

    @Override
    public Class<? extends GuiScreen> getGui() {
        return GuiItemCapabilityProxy.class;
    }
}
