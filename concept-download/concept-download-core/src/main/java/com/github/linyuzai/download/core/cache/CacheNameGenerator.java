package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 缓存名称生成器。
 */
public interface CacheNameGenerator {

    /**
     * 生成一个缓存名称。
     *
     * @param downloadable {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return 缓存名称
     */
    String generate(Downloadable downloadable, DownloadContext context);
}
