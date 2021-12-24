package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * 缓存名称生成器 / Cache name generator
 */
public interface CacheNameGenerator {

    /**
     * @param downloadable 可下载的资源 / Downloadable
     * @param context      下载上下文 / Content of download
     * @return 缓存名称 / Cache name
     */
    String generate(Downloadable downloadable, DownloadContext context);
}
