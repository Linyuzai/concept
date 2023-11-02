package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContext} 销毁器，{@link DownloadContext} 销毁时会回调。
 */
public interface DownloadContextDestroyer {

    /**
     * 销毁。
     *
     * @param context {@link DownloadContext}
     */
    void destroy(DownloadContext context);
}
