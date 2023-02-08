package com.github.linyuzai.domain.core.cache;

/**
 * 缓存适配器
 */
public interface CacheAdapter {

    /**
     * 是否支持 key
     */
    boolean support(Object key);

    /**
     * 获得缓存
     */
    <T> Cache<T> adapt(Object key);
}
