package dev.eeasee.translucence.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.function.Supplier;

public class MapUtil {

    public static <T> T getOrCreateInIntMap(Int2ObjectMap<T> map, int key, Supplier<T> supplier) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            T value = supplier.get();
            map.put(key, value);
            return value;
        }
    }
}
