package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import lombok.AllArgsConstructor;

/**
 * 缓存名称生成器初始化器 / Initializer of cache name generator
 */
@AllArgsConstructor
public class CacheNameGeneratorInitializer implements DownloadContextInitializer {

    private CacheNameGenerator cacheNameGenerator;

    /**
     * 将缓存名称生成器设置到下载上下文 / Set the cache name generator to the download context
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public void initialize(DownloadContext context) {
        context.set(CacheNameGenerator.class, cacheNameGenerator);
    }
}
