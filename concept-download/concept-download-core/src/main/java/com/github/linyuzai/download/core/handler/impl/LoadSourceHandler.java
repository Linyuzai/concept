package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.load.AfterSourceLoadedEvent;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 加载处理器 / A handler to process loads
 */
@AllArgsConstructor
public class LoadSourceHandler implements DownloadHandler {

    private SourceLoader sourceLoader;

    /**
     * 将所有的Source封装成对应的加载器 / Encapsulate all sources into corresponding loaders
     * 使用Invoker调用加载器 / Invoking the loader using invoker
     * 处理加载异常 / Handle load exception
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        Source source = context.get(Source.class);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        return sourceLoader.load(source, context)
                .doOnSuccess(it -> publisher.publish(new AfterSourceLoadedEvent(context, it)))
                .flatMap(it -> {
                    context.set(Source.class, it);
                    return chain.next(context);
                });
    }

    @Override
    public int getOrder() {
        return ORDER_LOAD_SOURCE;
    }
}
