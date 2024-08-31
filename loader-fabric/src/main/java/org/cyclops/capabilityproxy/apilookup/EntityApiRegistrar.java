package org.cyclops.capabilityproxy.apilookup;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.fabricmc.fabric.impl.lookup.entity.EntityApiLookupImpl;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.capabilityproxy.blockentity.BlockEntityEntityCapabilityProxyFabric;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A helper class to register delegating block apis for the given block entity
 * @author rubensworks
 */
public class EntityApiRegistrar {

    public static final EntityApiLookup<Storage<ItemVariant>, @Nullable Direction> SIDED =
            EntityApiLookup.get(ResourceLocation.fromNamespaceAndPath("fabric", "sided_item_storage"), Storage.asClass(), Direction.class);
    static {
        // TODO: this is just for testing.
        SIDED.registerForType((entity, context) -> InventoryStorage.of(entity.getInventory(), context), EntityType.PLAYER);
    }

    private boolean initialized = false;

    public void initializeCapabilityRegistrationsIfNeeded(BlockEntityType<? extends BlockEntityEntityCapabilityProxyFabric> blockEntityType) {
        if (!initialized) {
            initialized = true;
        }

        Map<ResourceLocation, EntityApiLookup<?, ?>> entityCapabilities = TypedApiHelpers.getTypedApiLookupsKeyed((Class<EntityApiLookup<?, ?>>) (Class) EntityApiLookupImpl.class);
        Map<ResourceLocation, BlockApiLookup<?, ?>> blockCapabilities = TypedApiHelpers.getTypedApiLookupsKeyed((Class<BlockApiLookup<?, ?>>) (Class) BlockApiLookupImpl.class);

        BlockEntityEntityCapabilityProxyFabric.BLOCK_TO_ENTITY_CAPABILITIES = Maps.newIdentityHashMap();
        for (Map.Entry<ResourceLocation, BlockApiLookup<?, ?>> entry : blockCapabilities.entrySet()) {
            registerCapability(blockEntityType, entry.getValue());

            // Heuristically try to match block caps with entity caps
            EntityApiLookup<?, ?> entityCapability = entityCapabilities.get(entry.getKey());
            if (entityCapability != null) {
                BlockEntityEntityCapabilityProxyFabric.BLOCK_TO_ENTITY_CAPABILITIES.put(entry.getValue(), entityCapability);
            }
        }
    }

    public <T, C> void registerCapability(BlockEntityType<? extends BlockEntityEntityCapabilityProxyFabric> blockEntityType, BlockApiLookup<T, C> blockApiLookup) {
        blockApiLookup.registerForBlockEntity((blockEntity, context) -> blockEntity.getCapability(blockApiLookup), blockEntityType);
    }

}
