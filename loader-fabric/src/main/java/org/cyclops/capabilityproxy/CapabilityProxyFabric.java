package org.cyclops.capabilityproxy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxyConfig;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxyConfig;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxyConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyFabric;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyFabricConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityEntityCapabilityProxyFabric;
import org.cyclops.capabilityproxy.blockentity.BlockEntityEntityCapabilityProxyFabricConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityItemCapabilityProxyFabric;
import org.cyclops.capabilityproxy.blockentity.BlockEntityItemCapabilityProxyFabricConfig;
import org.cyclops.capabilityproxy.blockentity.BlockEntityRangedCapabilityProxyFabric;
import org.cyclops.capabilityproxy.blockentity.BlockEntityRangedCapabilityProxyFabricConfig;
import org.cyclops.capabilityproxy.inventory.container.ContainerItemCapabilityProxyConfig;
import org.cyclops.capabilityproxy.proxy.ClientProxyFabric;
import org.cyclops.capabilityproxy.proxy.CommonProxyFabric;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

/**
 * The main mod class of CapabilityProxy.
 * @author rubensworks
 */
public class CapabilityProxyFabric extends ModBaseFabric<CapabilityProxyFabric> implements ModInitializer {

    /**
     * The unique instance of this mod.
     */
    public static CapabilityProxyFabric _instance;

    public static EntityApiLookup<EnergyStorage, @Nullable Void> ENERGY_STORAGE_ENTITY =
            EntityApiLookup.get(ResourceLocation.fromNamespaceAndPath("teamreborn", "energy"), EnergyStorage.class, Void.class);

    public CapabilityProxyFabric() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyFabric();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyFabric();
    }

    @Override
    protected boolean hasDefaultCreativeModeTab() {
        return true;
    }

    @Override
    protected CreativeModeTab.Builder constructDefaultCreativeModeTab(CreativeModeTab.Builder builder) {
        return super.constructDefaultCreativeModeTab(builder)
                .icon(() -> new ItemStack(RegistryEntries.ITEM_CAPABILITY_PROXY));
    }

    @Override
    protected void onConfigsRegister(ConfigHandlerCommon configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));

        configHandler.addConfigurable(new BlockCapabilityProxyConfig<>(this, BlockEntityCapabilityProxyFabric::new));
        configHandler.addConfigurable(new org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxyConfig<>(this, BlockEntityEntityCapabilityProxyFabric::new));
        configHandler.addConfigurable(new BlockItemCapabilityProxyConfig<>(this, BlockEntityItemCapabilityProxyFabric::new));
        configHandler.addConfigurable(new BlockRangedCapabilityProxyConfig<>(this, BlockEntityRangedCapabilityProxyFabric::new));

        configHandler.addConfigurable(new BlockEntityCapabilityProxyFabricConfig());
        configHandler.addConfigurable(new BlockEntityEntityCapabilityProxyFabricConfig());
        configHandler.addConfigurable(new BlockEntityItemCapabilityProxyFabricConfig());
        configHandler.addConfigurable(new BlockEntityRangedCapabilityProxyFabricConfig());

        configHandler.addConfigurable(new ContainerItemCapabilityProxyConfig<>(this));
    }
}
