package org.cyclops.capabilityproxy.apilookup;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.custom.ApiLookupMap;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.fabricmc.fabric.impl.lookup.custom.ApiLookupMapImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.blockentity.BlockEntityCapabilityProxyFabric;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * A helper class to register delegating block apis for the given block entity
 * @author rubensworks
 */
public class BlockApiRegistrar {

    private boolean initialized = false;

    public void initializeCapabilityRegistrationsIfNeeded(BlockEntityType<? extends BlockEntityCapabilityProxyFabric> blockEntityType) {
        if (!initialized) {
            initialized = true;
        }

        // The following force-loads the default Fabric capabilities
        FluidStorage.SIDED.apiClass();
        ItemStorage.SIDED.apiClass();

        for (BlockApiLookup<?, ?> capability : getBlockApiLookups()) {
            registerCapability(blockEntityType, capability);
        }
    }

    public <T, C> void registerCapability(BlockEntityType<? extends BlockEntityCapabilityProxyFabric> blockEntityType, BlockApiLookup<T, C> blockApiLookup) {
        blockApiLookup.registerForBlockEntity((blockEntity, context) -> blockEntity.getCapability(blockApiLookup, context), blockEntityType);
    }

    public static ApiLookupMap<BlockApiLookup<?, ?>> getBlockApiLookupsMap() {
        try {
            Field lookupsField = BlockApiLookupImpl.class.getDeclaredField("LOOKUPS");
            lookupsField.setAccessible(true);
            return (ApiLookupMap<BlockApiLookup<?, ?>>) lookupsField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <L> Map<ResourceLocation, Object> getApiLookupMap(ApiLookupMapImpl lookupMap) {
        try {
            Field lookupsField = ApiLookupMapImpl.class.getDeclaredField("lookups");
            lookupsField.setAccessible(true);
            return (Map<ResourceLocation, Object>) lookupsField.get(lookupMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Collection<BlockApiLookup<?, ?>> getBlockApiLookups() {
        Collection<BlockApiLookup<?, ?>> lookups = Lists.newArrayList();
        Map<ResourceLocation, Object> map = getApiLookupMap((ApiLookupMapImpl) getBlockApiLookupsMap());
        for (ResourceLocation id : map.keySet()) {
            Object lookupHolder = map.get(id);
            try {
                Field lookupField = lookupHolder.getClass().getDeclaredField("lookup");
                lookupField.setAccessible(true);
                lookups.add((BlockApiLookup) lookupField.get(lookupHolder));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return lookups;
    }

}
