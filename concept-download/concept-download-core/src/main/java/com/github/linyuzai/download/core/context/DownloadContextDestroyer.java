package com.github.linyuzai.download.core.context;

/**
 * 上下文销毁器 / Destroyer of context
 */
public interface DownloadContextDestroyer {

    /**
     * 销毁处理 / destroy
     *
     * @param context 下载上下文 / Context of download
     */
    void destroy(DownloadContext context);
}
