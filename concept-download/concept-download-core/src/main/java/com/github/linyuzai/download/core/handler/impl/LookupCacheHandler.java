package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import reactor.core.publisher.Mono;

public class LookupCacheHandler implements DownloadHandler {

    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        return chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_LOOKUP_CACHE;
    }
}
