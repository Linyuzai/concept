package com.github.linyuzai.domain.core.cache;

/**
 * 缓存提供者
 */
public interface CacheProvider {

    /**
     * 根据 key 获得对应的缓存
     */
    <T> Cache<T> get(Object key);
}
