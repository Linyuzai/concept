package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.source.Source;

import java.io.File;

/**
 * 支持 {@link Source} 和 {@link Compression} 的缓存处理。
 * <p>
 * Supports cache processing for {@link Source} and {@link Compression}.
 */
public interface Cacheable {

    /**
     * 默认的缓存路径。
     * <p>
     * Default cache path.
     */
    String PATH = new File(System.getProperty("user.home"), "concept/download").getAbsolutePath();

    /**
     * 是否启用缓存。
     * <p>
     * Enable caching.
     *
     * @return 如果启用缓存则返回 true
     * <p>
     * Return true if caching is enabled
     */
    default boolean isCacheEnabled() {
        return false;
    }

    /**
     * 缓存是否存在。
     * <p>
     * Does the cache exist.
     *
     * @return 如果缓存存在则返回 true
     * <p>
     * Return true if the cache exists
     */
    default boolean isCacheExisted() {
        return false;
    }

    /**
     * 缓存路径。
     * <p>
     * The path of cache.
     *
     * @return 如果存在缓存则返回缓存路径，否则返回 null
     * <p>
     * If there is a cache, the cache path is returned;
     * otherwise, null is returned
     */
    default String getCachePath() {
        return null;
    }

    /**
     * 删除缓存。
     * <p>
     * Delete the cache.
     */
    default void deleteCache() {

    }
}
