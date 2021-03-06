package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 缓存名称生成器。
 *
 * @see TimestampCacheNameGenerator
 */
public interface CacheNameGenerator {

    /**
     * 生成一个缓存名称。
     *
     * @param resource {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return 缓存名称
     */
    String generate(Resource resource, DownloadContext context);
}
