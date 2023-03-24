package com.github.linyuzai.domain.core.mock;

import com.github.linyuzai.domain.core.DomainContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class MockDomainContext implements DomainContext {

    private final Map<Object, Object> map = new LinkedHashMap<>();

    public MockDomainContext(Map<?, ?> map) {
        this.map.putAll(map);
    }

    public MockDomainContext(Object key, Object value) {
        this.map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> type) {
        return (T) map.get(type);
    }
}
