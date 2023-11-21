package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.concept.AbstractDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.logger.DownloadLogger;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 基于 {@link DownloadHandlerChain} 的 {@link DownloadConcept} 实现。
 */
public class ReactiveDownloadConcept extends AbstractDownloadConcept {

    public ReactiveDownloadConcept(DownloadContextFactory contextFactory, List<DownloadHandler> handlers, DownloadEventPublisher eventPublisher, DownloadLogger logger) {
        super(contextFactory, handlers, eventPublisher, logger);
    }

    @SuppressWarnings("all")
    @Override
    protected Object doDownload(DownloadContext context, List<DownloadHandler> handlers, Runnable onComplete) {
        Object object = new ReactiveDownloadHandlerChain(0, handlers).next(context);
        ((Mono<?>) object).doAfterTerminate(onComplete);
        return object;
    }
}
