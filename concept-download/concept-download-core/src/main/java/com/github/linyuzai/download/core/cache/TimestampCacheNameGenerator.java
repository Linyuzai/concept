package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 基于时间戳的 {@link CacheNameGenerator}。
 * <p>
 * Timestamp based {@link CacheNameGenerator}.
 */
public class TimestampCacheNameGenerator extends AbstractCacheNameGenerator {

    /**
     * 将使用当前时间戳作为名称返回。
     * <p>
     * Returns the current timestamp as the name.
     *
     * @param downloadable {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return 时间戳转 {@link String}
     * <p>
     * Timestamp to {@link String}
     */
    @Override
    public String doGenerate(Downloadable downloadable, DownloadContext context) {
        return String.valueOf(System.currentTimeMillis());
    }
}
