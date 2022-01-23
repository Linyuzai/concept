package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import lombok.AllArgsConstructor;

/**
 * 将 {@link CacheNameGenerator} 设置到 {@link DownloadContext} 中。
 * <p>
 * Set {@link CacheNameGenerator} to {@link DownloadContext}.
 */
@AllArgsConstructor
public class CacheNameGeneratorInitializer implements DownloadContextInitializer {

    private CacheNameGenerator cacheNameGenerator;

    /**
     * 在 {@link DownloadContext} 初始化时，
     * 设置 {@link CacheNameGenerator}。
     * <p>
     * When {@link DownloadContext} is initialized,
     * set {@link CacheNameGenerator}.
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void initialize(DownloadContext context) {
        context.set(CacheNameGenerator.class, cacheNameGenerator);
    }
}
