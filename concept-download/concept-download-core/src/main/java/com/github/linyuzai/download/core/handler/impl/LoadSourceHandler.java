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
 * 对所有的 {@link Source} 进行加载。
 */
@AllArgsConstructor
public class LoadSourceHandler implements DownloadHandler {

    /**
     * 加载器。
     */
    private SourceLoader sourceLoader;

    /**
     * 加载 {@link Source}。
     * 使用 {@link SourceLoader} 加载所有的 {@link Source}，
     * 发布 {@link AfterSourceLoadedEvent} 事件，
     * 设置新的 {@link Source} 到 {@link DownloadContext} 中。
     *
     * @param context {@link DownloadContext}
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
