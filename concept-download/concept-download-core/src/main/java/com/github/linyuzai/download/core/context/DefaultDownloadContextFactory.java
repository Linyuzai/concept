package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * 默认实现的 {@link DownloadContextFactory}。
 * <p>
 * Default implementation of {@link DownloadContextFactory}.
 */
@AllArgsConstructor
public class DefaultDownloadContextFactory implements DownloadContextFactory {

    @NonNull
    private List<DownloadContextInitializer> initializers;

    @NonNull
    private List<DownloadContextDestroyer> destroyers;

    /**
     * 创建一个 {@link DefaultDownloadContext}。
     * 使用 {@link UUID} 生成唯一ID。
     * <p>
     * Create a {@link DefaultDownloadContext}.
     * Use {@link UUID} to generate a unique ID.
     *
     * @param options {@link DownloadOptions}
     * @return {@link DefaultDownloadContext}
     */
    @Override
    public DownloadContext create(DownloadOptions options) {
        DefaultDownloadContext context = new DefaultDownloadContext(UUID.randomUUID().toString(), options);
        context.setInitializers(initializers);
        context.setDestroyers(destroyers);
        return context;
    }
}
