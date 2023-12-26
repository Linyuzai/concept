package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.event.DownloadLifecycleListener;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 将任意的对象解析成对应的 {@link Source}。
 */
@Getter
@RequiredArgsConstructor
public class CreateSourceHandler implements DownloadHandler, DownloadLifecycleListener {

    /**
     * {@link SourceFactory} 适配器
     */
    private final SourceFactoryAdapter sourceFactoryAdapter;

    /**
     * 创建 {@link Source}。
     * 通过 {@link SourceFactoryAdapter} 获得适配的 {@link SourceFactory}，
     * 通过 {@link SourceFactory} 创建对应的 {@link Source}，
     * 将 {@link Source} 设置到 {@link DownloadContext} 中，
     * 发布 {@link SourceCreatedEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public Object handle(DownloadContext context, DownloadHandlerChain chain) {
        if (context.contains(Compression.class)) {
            return chain.next(context);
        }
        DownloadOptions options = DownloadOptions.get(context);
        Object original = options.getSource();
        SourceFactory factory = sourceFactoryAdapter.getFactory(original, context);
        Source source = factory.create(original, context);
        context.set(Source.class, source);
        DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
        publisher.publish(new SourceCreatedEvent(context, source));
        return chain.next(context);
    }

    /**
     * 初始化时将 {@link SourceFactoryAdapter} 设置到 {@link DownloadContext} 中。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void onStart(DownloadContext context) {
        context.set(SourceFactoryAdapter.class, sourceFactoryAdapter);
    }

    /**
     * 销毁时，如果需要则删除缓存并发布 {@link SourceCacheDeletedEvent} 事件；
     * 释放资源并发布 {@link SourceReleasedEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void onComplete(DownloadContext context) {
        Source source = context.get(Source.class);
        if (source != null) {
            DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
            DownloadOptions options = DownloadOptions.get(context);
            boolean delete = options.isSourceCacheDelete();
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
}
