package org.cyclops.capabilityproxy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * An entity capability proxy.
 * @author josephcsible
 */
public class BlockEntityEntityCapabilityProxyNeoForge extends BlockEntityEntityCapabilityProxyCommon {

    public static Map<BlockCapability<?, ?>, EntityCapability<?, ?>> BLOCK_TO_ENTITY_CAPABILITIES;

    public BlockEntityEntityCapabilityProxyNeoForge(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    protected <T, C> List<Entity> getEntities(EntityCapability<T, C> capability) {
        AABB aabb = new AABB(getBlockPos().relative(getFacing()));
        return getLevel().getEntitiesOfClass(Entity.class, aabb, entity -> entity.getCapability(capability, capability.contextClass() == Direction.class ? (C) getFacing().getOpposite() : null) != null);
    }

    public <T, C1, C2> T getCapability(BlockCapability<T, C1> blockCapability) {
        EntityCapability<T, C2> entityCapability = blockCapabilityToEntityCapability(blockCapability);
        if (entityCapability == null) {
            return null;
        }
        List<Entity> entities = getEntities(entityCapability);
        if (entities.isEmpty()) {
            return null;
        }
        Entity entity = entities.get(0);
        return entity.getCapability(entityCapability, entityCapability.contextClass() == Direction.class ? (C2) getFacing().getOpposite() : null);
    }

    @Nullable
    public static <T, C1, C2> EntityCapability<T, C2> blockCapabilityToEntityCapability(BlockCapability<T, C1> capability) {
        return (EntityCapability<T, C2>) BLOCK_TO_ENTITY_CAPABILITIES.get(capability);
    }
}
