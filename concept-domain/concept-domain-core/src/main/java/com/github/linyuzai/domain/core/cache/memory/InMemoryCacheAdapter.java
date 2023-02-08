package com.github.linyuzai.domain.core.cache.memory;


import com.github.linyuzai.domain.core.cache.Cache;
import com.github.linyuzai.domain.core.cache.CacheAdapter;

/**
 * 内存缓存适配器
 */
public class InMemoryCacheAdapter implements CacheAdapter {

    /**
     * 全部支持
     */
    @Override
    public boolean support(Object key) {
        return true;
    }

    /**
     * 返回内存缓存实现
     */
    @Override
    public <T> Cache<T> adapt(Object key) {
        return new InMemoryCache<>();
    }
}
