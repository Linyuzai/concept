package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadLifecycleListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 将 {@link CacheNameGenerator} 设置到 {@link DownloadContext} 中。
 */
@Getter
@RequiredArgsConstructor
public class CacheNameGeneratorInitializer implements DownloadLifecycleListener {

    /**
     * 需要设置的 {@link CacheNameGenerator}
     */
    private final CacheNameGenerator cacheNameGenerator;

    /**
     * 设置 {@link CacheNameGenerator}。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void onStart(DownloadContext context) {
        context.set(CacheNameGenerator.class, cacheNameGenerator);
    }
}
