package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import reactor.core.publisher.Mono;

/**
 * 下载处理链 / Chain of handler to download
 */
public interface DownloadHandlerChain {

    /**
     * 调度下一个下载处理器 / To execute next handler
     *
     * @param context 下载上下文 / Context of download
     */
    Mono<Void> next(DownloadContext context);
}
