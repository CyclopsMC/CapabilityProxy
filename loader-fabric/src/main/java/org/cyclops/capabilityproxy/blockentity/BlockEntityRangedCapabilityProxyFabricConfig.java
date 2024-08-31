package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxyFabric;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.apilookup.BlockApiRegistrar;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityRangedCapabilityProxyFabric}.
 * @author rubensworks
 *
 */
public class BlockEntityRangedCapabilityProxyFabricConfig extends BlockEntityConfigCommon<BlockEntityRangedCapabilityProxyFabric, CapabilityProxyFabric> {

    public static BlockApiRegistrar blockApiRegistrar;

    public BlockEntityRangedCapabilityProxyFabricConfig() {
        super(
                CapabilityProxyFabric._instance,
                "ranged_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityRangedCapabilityProxyFabric::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()), null)
        );
        blockApiRegistrar = new BlockApiRegistrar();
    }

}
