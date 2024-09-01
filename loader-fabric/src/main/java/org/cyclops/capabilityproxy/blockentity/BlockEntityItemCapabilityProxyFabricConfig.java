package org.cyclops.capabilityproxy.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.CapabilityProxyFabric;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.apilookup.ItemApiRegistrar;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;

/**
 * Config for the {@link BlockEntityItemCapabilityProxyFabric}.
 * @author rubensworks
 *
 */
public class BlockEntityItemCapabilityProxyFabricConfig extends BlockEntityConfigCommon<BlockEntityItemCapabilityProxyFabric, CapabilityProxyFabric> {

    public static ItemApiRegistrar itemApiRegistrar;

    public BlockEntityItemCapabilityProxyFabricConfig() {
        super(
                CapabilityProxyFabric._instance,
                "item_capability_proxy",
                (eConfig) -> new BlockEntityType<>(BlockEntityItemCapabilityProxyFabric::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY.value()), null)
        );
        itemApiRegistrar = new ItemApiRegistrar();
    }

}
