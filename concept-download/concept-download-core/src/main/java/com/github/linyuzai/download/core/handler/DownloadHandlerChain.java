package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import reactor.core.publisher.Mono;

/**
 * 下载处理链。
 */
public interface DownloadHandlerChain {

    /**
     * 调度下一个下载处理器
     *
     * @param context {@link DownloadContext}
     */
    Mono<Void> next(DownloadContext context);
}
