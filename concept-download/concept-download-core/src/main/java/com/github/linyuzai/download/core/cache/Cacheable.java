package com.github.linyuzai.download.core.cache;

import java.io.File;

/**
 * 代表一个可缓存的对象 / Mark an object which may need cache
 * 用于可下载的对象 / Used for {@link com.github.linyuzai.download.core.concept.Downloadable}
 * 下载的源文件 / Original file {@link com.github.linyuzai.download.core.source.Source}
 * 压缩后的文件 / Compressed file {@link com.github.linyuzai.download.core.compress.Compressible}
 */
public interface Cacheable {

    /**
     * 默认的缓存地址 / Default cache path
     */
    String PATH = new File(System.getProperty("user.home"), "DownloadCache").getAbsolutePath();

    /**
     * @return 是否启用缓存 / If enable cache
     */
    boolean isCacheEnabled();

    /**
     * @return 缓存是否存在 / Cache if existed
     */
    default boolean isCacheExisted() {
        return true;
    }

    /**
     * @return 缓存路径 / The path of cache
     */
    String getCachePath();

    /**
     * 删除缓存 / Delete the cache
     */
    default void deleteCache() {

    }
}
