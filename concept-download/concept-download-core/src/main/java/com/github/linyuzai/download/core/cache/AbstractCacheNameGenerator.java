package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import org.springframework.util.StringUtils;

/**
 * 将会优先使用 {@link Source} 或 {@link Compression} 的名称。
 * <p>
 * The name of {@link Source} or {@link Compression} will take precedence.
 */
public abstract class AbstractCacheNameGenerator implements CacheNameGenerator {

    /**
     * 尝试获取 {@link Source} 或 {@link Compression} 的名称，
     * 如果不为 null 或空，则直接使用该名称作为缓存的名称，
     * 否则再调用自定义的生成方法。
     * <p>
     * Try to get the name of {@link Source} or {@link Compression}.
     * If it is not null or empty, directly use the name as the name of the cache,
     * otherwise call the custom generation method.
     *
     * @param downloadable {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return {@link Source} 的名称或 {@link Compression} 的名称或自定义生成的名称
     * <p>
     * The name of {@link Source} or the name of {@link Compression} or the name of a custom generate
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
     * <p>
     * You need to customize the name generation method of the implementation.
     *
     * @param downloadable {@link Source} / {@link Compression}
     * @param context      {@link DownloadContext}
     * @return 自定义生成的名称
     * <p>
     * the name of a custom generate
     */
    public abstract String doGenerate(Downloadable downloadable, DownloadContext context);
}
