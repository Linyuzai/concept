package com.github.linyuzai.domain.core;

import java.util.function.Supplier;

public interface DomainProperties {

    default <V> V getProperty(Object key) {
        return null;
    }

    default void setProperty(Object key, Object value) {

    }

    default boolean hasProperty(Object key) {
        return false;
    }

    default <V> V withPropertyKey(Object key, Supplier<V> supplier) {
        if (hasProperty(key)) {
            return getProperty(key);
        }
        V v = supplier.get();
        setProperty(key, v);
        return v;
    }

    default <V> V withPropertyValue(Object key, Supplier<V> supplier) {
        V property = getProperty(key);
        if (property == null) {
            V v = supplier.get();
            setProperty(key, v);
            return v;
        }
        return property;
    }
}
