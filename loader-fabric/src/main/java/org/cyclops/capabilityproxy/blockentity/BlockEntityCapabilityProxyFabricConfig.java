package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxyFabric;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.apilookup.BlockApiRegistrar;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityCapabilityProxyFabric}.
 * @author rubensworks
 *
 */
public class BlockEntityCapabilityProxyFabricConfig extends BlockEntityConfigCommon<BlockEntityCapabilityProxyFabric, CapabilityProxyFabric> {

    @ConfigurablePropertyCommon(category = "machine", comment = "If the item storage capability should be exposed for player inventories.", configLocation = ModConfigLocation.COMMON)
    public static boolean registerPlayerItemStorage = true;

    public static BlockApiRegistrar blockApiRegistrar;

    public BlockEntityCapabilityProxyFabricConfig() {
        super(
                CapabilityProxyFabric._instance,
                "capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityCapabilityProxyFabric::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_CAPABILITY_PROXY.value()), null)
        );
        blockApiRegistrar = new BlockApiRegistrar();
    }

}
