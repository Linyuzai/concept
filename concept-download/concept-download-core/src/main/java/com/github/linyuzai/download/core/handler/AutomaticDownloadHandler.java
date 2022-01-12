package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;
import reactor.core.publisher.Mono;

/**
 * 自动执行调用链 / Auto execute call chain
 * 支持拦截 / Support interception
 */
public interface AutomaticDownloadHandler extends DownloadHandler {

    @Override
    default Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        return doHandle(context).flatMap(it -> chain.next(context));
    }

    Mono<Void> doHandle(DownloadContext context);
}
