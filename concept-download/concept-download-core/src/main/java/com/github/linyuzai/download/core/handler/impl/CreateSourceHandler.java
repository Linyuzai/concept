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
 * 将任意的对象解析成对应的 {@link Source}。
 */
@AllArgsConstructor
public class CreateSourceHandler implements DownloadHandler, DownloadContextInitializer, DownloadContextDestroyer {

    /**
     * {@link SourceFactory} 适配器
     */
    private SourceFactoryAdapter sourceFactoryAdapter;

    /**
     * 创建 {@link Source}。
     * 通过 {@link SourceFactoryAdapter} 获得适配的 {@link SourceFactory}，
     * 通过 {@link SourceFactory} 创建对应的 {@link Source}，
     * 将 {@link Source} 设置到 {@link DownloadContext} 中，
     * 发布 {@link AfterSourceCreatedEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        Object original = context.getOptions().getSource();
        SourceFactory factory = sourceFactoryAdapter.getFactory(original, context);
        Source source = factory.create(original, context);
        context.set(Source.class, source);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new AfterSourceCreatedEvent(context, source));
        return chain.next(context);
    }

    /**
     * 初始化时将 {@link SourceFactoryAdapter} 设置到 {@link DownloadContext} 中。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void initialize(DownloadContext context) {
        context.set(SourceFactoryAdapter.class, sourceFactoryAdapter);
    }

    /**
     * 销毁时，如果需要则删除缓存并发布 {@link SourceCacheDeletedEvent} 事件；
     * 释放资源并发布 {@link SourceReleasedEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void destroy(DownloadContext context) {
        Source source = context.get(Source.class);
        if (source != null) {
            DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
            boolean delete = context.getOptions().isSourceCacheDelete();
            //是否删除缓存
            if (delete) {
                source.deleteCache();
                publisher.publish(new SourceCacheDeletedEvent(context, source));
            }
            //释放资源
            source.release();
            publisher.publish(new SourceReleasedEvent(context, source));
        }
    }

    @Override
    public int getOrder() {
        return ORDER_CREATE_SOURCE;
    }
}
