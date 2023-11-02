package com.github.linyuzai.download.core.context;

/**
 * {@link DownloadContextFactory} 的默认实现。
 */
public class DefaultDownloadContextFactory implements DownloadContextFactory {

    /**
     * 创建一个 {@link DefaultDownloadContext}。
     *
     * @return {@link DefaultDownloadContext}
     */
    @Override
    public DownloadContext create() {
        return new DefaultDownloadContext();
    }
}
