package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.source.Source;

import java.io.File;

/**
 * 支持 {@link Source} 和 {@link Compression} 的缓存处理。
 */
public interface Cacheable {

    /**
     * 默认的缓存路径。
     */
    String PATH = new File(System.getProperty("user.home"), "concept/download").getAbsolutePath();

    /**
     * 是否启用缓存。
     *
     * @return 如果启用缓存则返回 true
     */
    default boolean isCacheEnabled() {
        return false;
    }

    /**
     * 缓存是否存在。
     *
     * @return 如果缓存存在则返回 true
     */
    default boolean isCacheExisted() {
        return false;
    }

    /**
     * 缓存路径。
     *
     * @return 如果存在缓存则返回缓存路径，否则返回 null
     */
    default String getCachePath() {
        return null;
    }

    /**
     * 删除缓存。
     */
    default void deleteCache() {

    }
}
