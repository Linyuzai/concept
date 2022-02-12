package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;

/**
 * {@link DownloadContext} 工厂。
 */
public interface DownloadContextFactory {

    /**
     * 创建一个 {@link DownloadContext}。
     *
     * @param options {@link DownloadOptions}
     * @return {@link DownloadContext}
     */
    DownloadContext create(DownloadOptions options);
}
