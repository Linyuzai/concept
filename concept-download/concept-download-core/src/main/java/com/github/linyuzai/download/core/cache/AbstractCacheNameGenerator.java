package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import org.springframework.util.StringUtils;

/**
 * 将会优先使用 {@link Source} 或 {@link Compression} 的名称。
 */
public abstract class AbstractCacheNameGenerator implements CacheNameGenerator {

    /**
     * 尝试获取 {@link Source} 或 {@link Compression} 的名称，
     * 如果不为 null 或空，则直接使用该名称作为缓存的名称，
     * 否则再调用自定义的生成方法。
     *
     * @param downloadable {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return {@link Source} 的名称或 {@link Compression} 的名称或自定义生成的名称
     */
    @Override
    public String generate(Downloadable downloadable, DownloadContext context) {
        String name = downloadable.getName();
        if (StringUtils.hasText(name)) {
            return name;
        } else {
            return doGenerate(downloadable, context);
        }
    }

    /**
     * 需要自定义实现的名称生成方法。
     *
     * @param downloadable {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return 自定义生成的名称
     */
    public abstract String doGenerate(Downloadable downloadable, DownloadContext context);
}
