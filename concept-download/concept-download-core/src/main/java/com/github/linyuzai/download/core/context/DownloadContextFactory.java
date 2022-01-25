package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;

/**
 * {@link DownloadContext} 工厂。
 * <p>
 * Factory of {@link DownloadContext}.
 */
public interface DownloadContextFactory {

    /**
     * 创建一个 {@link DownloadContext}。
     * <p>
     * Create a {@link DownloadContext}.
     *
     * @param options {@link DownloadOptions}
     * @return {@link DownloadContext}
     */
    DownloadContext create(DownloadOptions options);
}
