package com.github.linyuzai.domain.core.cache.memory;

import com.github.linyuzai.domain.core.cache.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存缓存
 *
 * @param <T>
 */
public class InMemoryCache<T> implements Cache<T> {

    private final Map<String, T> cache = new ConcurrentHashMap<>();

    @Override
    public void set(String id, T cache) {
        if (cache == null) {
            return;
        }
        this.cache.put(id, cache);
    }

    @Override
    public T get(String id) {
        return this.cache.get(id);
    }

    @Override
    public boolean exist(String id) {
        return this.cache.containsKey(id);
    }

    @Override
    public void remove(String id) {
        this.cache.remove(id);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }
}
