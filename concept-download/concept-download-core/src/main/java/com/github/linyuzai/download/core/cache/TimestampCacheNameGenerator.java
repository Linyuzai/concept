package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 基于时间戳的 {@link CacheNameGenerator}。
 */
public class TimestampCacheNameGenerator extends AbstractCacheNameGenerator {

    /**
     * 将使用当前时间戳作为名称返回。
     *
     * @param resource {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return 时间戳转 {@link String}
     */
    @Override
    public String doGenerate(Resource resource, DownloadContext context) {
        return String.valueOf(System.currentTimeMillis());
    }
}
