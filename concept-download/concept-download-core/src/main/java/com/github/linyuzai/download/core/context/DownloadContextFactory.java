package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import reactor.core.publisher.Mono;

/**
 * 下载上下文工厂 / Factory of context
 */
public interface DownloadContextFactory {

    /**
     * 创建一个上下文 / Create a context
     *
     * @param options 下载操作参数 / Options of download
     * @return 下载上下文 / Context of download
     */
    DownloadContext create(DownloadOptions options);
}
