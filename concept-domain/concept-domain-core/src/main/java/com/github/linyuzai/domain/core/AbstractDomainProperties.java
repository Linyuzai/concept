package com.github.linyuzai.domain.core;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractDomainProperties implements DomainProperties {

    private final Map<Object, Object> properties = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getProperty(Object key) {
        return (V) properties.get(key);
    }

    @Override
    public void setProperty(Object key, Object value) {
        properties.put(key, value);
    }

    @Override
    public boolean hasProperty(Object key) {
        return properties.containsKey(key);
    }
}
