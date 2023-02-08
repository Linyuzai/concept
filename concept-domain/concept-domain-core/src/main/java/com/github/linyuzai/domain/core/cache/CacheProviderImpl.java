package com.github.linyuzai.domain.core.cache;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 缓存提供者通过缓存适配器来返回各种缓存
 */
@AllArgsConstructor
public class CacheProviderImpl implements CacheProvider {

    /**
     * 所有的缓存适配器
     */
    private List<CacheAdapter> cacheAdapters;

    /**
     * 按顺序进行匹配
     */
    @Override
    public <T> Cache<T> get(Object key) {
        for (CacheAdapter adapter : cacheAdapters) {
            if (adapter.support(key)) {
                return adapter.adapt(key);
            }
        }
        return Cache.disabled();
    }
}
