package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.event.*;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.logger.DownloadLogger;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class AbstractDownloadConcept implements DownloadConcept {

    /**
     * 上下文工厂
     */
    private final DownloadContextFactory contextFactory;

    /**
     * 处理器
     */
    private final List<DownloadHandler> handlers;

    private final DownloadEventPublisher eventPublisher;

    private final DownloadLogger logger;

    @Override
    public Object download(DownloadOptions options) {
        //创建上下文
        DownloadContext context = contextFactory.create();
        context.set(DownloadOptions.class, options);
        context.set(DownloadLogger.class, logger);
        DownloadEventPublisher publisher = delegateDownloadEventPublisher(options);
        context.set(DownloadEventPublisher.class, publisher);
        publisher.publish(new DownloadStartEvent(context));
        List<DownloadHandler> filtered = handlers.stream()
                .filter(it -> it.support(context))
                .collect(Collectors.toList());
        //处理链
        return doDownload(context, filtered, () ->
                publisher.publish(new DownloadSuccessEvent(context)), e ->
                publisher.publish(new DownloadErrorEvent(context, e)), () ->
                publisher.publish(new DownloadCompleteEvent(context)));
    }

    protected DownloadEventPublisher delegateDownloadEventPublisher(DownloadOptions options) {
        DownloadEventListener listener = options.getEventListener();
        if (listener == null) {
            return eventPublisher;
        } else {
            return new DownloadEventPublisherDelegate(eventPublisher, listener);
        }
    }

    protected abstract Object doDownload(DownloadContext context, List<DownloadHandler> handlers, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete);
}
