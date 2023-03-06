package com.github.linyuzai.cloud.web.core.context;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class WebContextImpl implements WebContext {

    private final Map<Object, Object> context = new LinkedHashMap<>();

    @Override
    public void put(Object key, Object value) {
        context.put(key, value);
    }

    @Override
    public <V> V get(Object key) {
        return (V) context.get(key);
    }

    @Override
    public <V> V get(Object key, V defaultValue) {
        return (V) context.getOrDefault(key, defaultValue);
    }

    @Override
    public void remove(Object key) {
        context.remove(key);
    }

    @Override
    public Map<Object, Object> toMap() {
        return Collections.unmodifiableMap(context);
    }

    @Override
    public void reset() {
        context.clear();
    }
}
