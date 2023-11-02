package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContext} 工厂。
 */
public interface DownloadContextFactory {

    /**
     * 创建一个 {@link DownloadContext}。
     *
     * @return {@link DownloadContext}
     */
    DownloadContext create();
}
