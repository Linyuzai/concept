package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.source.*;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 下载源处理拦截器
 */
@AllArgsConstructor
public class CreateSourceHandler implements DownloadHandler, DownloadContextInitializer, DownloadContextDestroyer {

    private SourceFactoryAdapter sourceFactoryAdapter;

    /**
     * 将所有需要下载的数据对象转换为下载源
     *
     * @param context 下载上下文
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        Object original = context.getOptions().getSource();
        SourceFactory factory = sourceFactoryAdapter.getFactory(original, context);
        Source source = factory.create(original, context);
        context.set(Source.class, source);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new SourceCreatedEvent(context, source));
        return chain.next(context);
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(SourceFactoryAdapter.class, sourceFactoryAdapter);
    }

    @Override
    public void destroy(DownloadContext context) {
        Source source = context.get(Source.class);
        if (source != null) {
            DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
            boolean delete = context.getOptions().isSourceCacheDelete();
            if (delete) {
                source.deleteCache();
                publisher.publish(new SourceCacheDeletedEvent(context, source));
            }
            source.release();
            publisher.publish(new SourceReleasedEvent(context, source));
        }
    }

    @Override
    public int getOrder() {
        return ORDER_CREATE_SOURCE;
    }
}
