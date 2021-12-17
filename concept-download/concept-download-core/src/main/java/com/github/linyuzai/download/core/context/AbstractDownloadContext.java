package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;

/**
 * 持有下载操作参数的下载上下文 / Context of download holding download options
 */
public abstract class AbstractDownloadContext implements DownloadContext {

    @Getter
    private final DownloadOptions options;

    /**
     * 上下文依赖一个下载操作的参数 / Context depend on a download options
     *
     * @param options 下载操作参数 / Options of download
     */
    public AbstractDownloadContext(DownloadOptions options) {
        this.options = options;
    }
}
