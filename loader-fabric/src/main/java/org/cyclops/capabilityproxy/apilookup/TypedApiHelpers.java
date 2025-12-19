package org.cyclops.capabilityproxy.apilookup;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.lookup.v1.custom.ApiLookupMap;
import net.fabricmc.fabric.impl.lookup.custom.ApiLookupMapImpl;
import net.minecraft.resources.Identifier;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author rubensworks
 */
public class TypedApiHelpers {

    public static <T> ApiLookupMap<T> getTypedApiLookupsMap(Class<T> clazz) {
        try {
            Field lookupsField = clazz.getDeclaredField("LOOKUPS");
            lookupsField.setAccessible(true);
            return (ApiLookupMap<T>) lookupsField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <L> Map<Identifier, Object> getApiLookupMap(ApiLookupMapImpl lookupMap) {
        try {
            Field lookupsField = ApiLookupMapImpl.class.getDeclaredField("lookups");
            lookupsField.setAccessible(true);
            return (Map<Identifier, Object>) lookupsField.get(lookupMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Map<Identifier, T> getTypedApiLookupsKeyed(Class<T> clazz) {
        Map<Identifier, T> lookups = Maps.newHashMap();
        Map<Identifier, Object> map = getApiLookupMap((ApiLookupMapImpl) getTypedApiLookupsMap(clazz));
        for (Identifier id : map.keySet()) {
            Object lookupHolder = map.get(id);
            try {
                Field lookupField = lookupHolder.getClass().getDeclaredField("lookup");
                lookupField.setAccessible(true);
                lookups.put(id, (T) lookupField.get(lookupHolder));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return lookups;
    }

    public static <T> Collection<T> getTypedApiLookups(Class<T> clazz) {
        return getTypedApiLookupsKeyed(clazz).values();
    }

}
