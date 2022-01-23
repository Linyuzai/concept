package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 缓存名称生成器。
 * <p>
 * Cache name generator.
 */
public interface CacheNameGenerator {

    /**
     * 生成一个缓存名称。
     * <p>
     * Generate a cache name.
     *
     * @param downloadable {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return 缓存名称
     * <p>
     * Cache name
     */
    String generate(Downloadable downloadable, DownloadContext context);
}
