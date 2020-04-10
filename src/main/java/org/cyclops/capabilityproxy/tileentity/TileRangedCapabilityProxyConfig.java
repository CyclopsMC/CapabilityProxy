package org.cyclops.capabilityproxy.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.capabilityproxy.CapabilityProxy;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.client.render.RenderTileRangedCapabilityProxy;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Config for the {@link TileRangedCapabilityProxy}.
 * @author rubensworks
 *
 */
public class TileRangedCapabilityProxyConfig extends TileEntityConfig<TileRangedCapabilityProxy> {

    public TileRangedCapabilityProxyConfig() {
        super(
                CapabilityProxy._instance,
                "ranged_capability_proxy",
                (eConfig) -> new TileEntityType<>(TileRangedCapabilityProxy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY), null)
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if(MinecraftHelpers.isClientSide()) {
            registerClientSide();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerClientSide() {
        CapabilityProxy._instance.getProxy().registerRenderer(getInstance(), RenderTileRangedCapabilityProxy::new);
    }

}
