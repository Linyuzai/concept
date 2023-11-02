package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContext} 初始化器，{@link DownloadContext} 初始化时会回调。
 */
public interface DownloadContextInitializer {

    /**
     * 初始化。
     *
     * @param context {@link DownloadContext}
     */
    void initialize(DownloadContext context);
}
