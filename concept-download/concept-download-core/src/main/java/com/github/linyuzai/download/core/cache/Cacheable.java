package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;

import java.io.File;

/**
 * 代表一个可缓存的对象 / Mark an object which may need cache
 * 用于可下载的对象 / Used for {@link Downloadable}
 * 下载的源文件 / Original file {@link Source}
 * 压缩后的文件 / Compressed file {@link Compression}
 */
public interface Cacheable {

    /**
     * 默认的缓存地址 / Default cache path
     */
    String PATH = new File(System.getProperty("user.home"), "DownloadCache").getAbsolutePath();

    /**
     * @return 是否启用缓存 / If enable cache
     */
    default boolean isCacheEnabled() {
        return false;
    }

    /**
     * @return 缓存是否存在 / Cache if existed
     */
    default boolean isCacheExisted() {
        return false;
    }

    /**
     * @return 缓存路径 / The path of cache
     */
    default String getCachePath() {
        return null;
    }

    /**
     * 删除缓存 / Delete the cache
     * 如果对应的属性为true / If the property is true {@link DownloadOptions#isCompressCacheDelete()} &
     * {@link DownloadOptions#isSourceCacheDelete()}
     * 就会在销毁时删除 / Will be deleted when destroyed {@link DownloadContextDestroyer#destroy(DownloadContext)}
     */
    default void deleteCache() {

    }
}
