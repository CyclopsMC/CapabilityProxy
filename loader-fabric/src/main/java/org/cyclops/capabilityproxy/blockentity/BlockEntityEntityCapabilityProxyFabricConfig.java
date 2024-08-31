package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxyFabric;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.apilookup.EntityApiRegistrar;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityEntityCapabilityProxyFabric}.
 * @author rubensworks
 *
 */
public class BlockEntityEntityCapabilityProxyFabricConfig extends BlockEntityConfigCommon<BlockEntityEntityCapabilityProxyFabric, CapabilityProxyFabric> {

    public static EntityApiRegistrar entityApiRegistrar;

    public BlockEntityEntityCapabilityProxyFabricConfig() {
        super(
                CapabilityProxyFabric._instance,
                "entity_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityEntityCapabilityProxyFabric::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY.value()), null)
        );
        entityApiRegistrar = new EntityApiRegistrar();
    }

}
