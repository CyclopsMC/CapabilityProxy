package org.cyclops.capabilityproxy.blockentity;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * An entity capability proxy.
 * @author josephcsible
 */
public class BlockEntityEntityCapabilityProxyFabric extends BlockEntityEntityCapabilityProxyCommon {

    public static Map<BlockApiLookup<?, ?>, EntityApiLookup<?, ?>> BLOCK_TO_ENTITY_CAPABILITIES;

    public BlockEntityEntityCapabilityProxyFabric(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
        BlockEntityEntityCapabilityProxyFabricConfig.entityApiRegistrar.initializeCapabilityRegistrationsIfNeeded((BlockEntityType<? extends BlockEntityEntityCapabilityProxyFabric>) getType());
    }

    protected <T, C> List<Entity> getEntities(EntityApiLookup<T, C> capability) {
        AABB aabb = new AABB(getBlockPos().relative(getFacing()));
        return getLevel().getEntitiesOfClass(Entity.class, aabb, entity -> capability.find(entity, capability.contextClass() == Direction.class ? (C) getFacing().getOpposite() : null) != null);
    }

    public <T, C1, C2> T getCapability(BlockApiLookup<T, C1> blockCapability) {
        EntityApiLookup<T, C2> entityCapability = blockCapabilityToEntityApiLookup(blockCapability);
        if (entityCapability == null) {
            return null;
        }
        List<Entity> entities = getEntities(entityCapability);
        if (entities.isEmpty()) {
            return null;
        }
        Entity entity = entities.get(0);
        return entityCapability.find(entity, entityCapability.contextClass() == Direction.class ? (C2) getFacing().getOpposite() : null);
    }

    @Nullable
    public static <T, C1, C2> EntityApiLookup<T, C2> blockCapabilityToEntityApiLookup(BlockApiLookup<T, C1> capability) {
        return (EntityApiLookup<T, C2>) BLOCK_TO_ENTITY_CAPABILITIES.get(capability);
    }
}
