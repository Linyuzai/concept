package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.concept.AbstractDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.executor.DownloadExecutor;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.logger.DownloadLogger;
import com.github.linyuzai.download.core.options.DownloadOptions;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * 基于 {@link DownloadHandlerChain} 的 {@link DownloadConcept} 实现。
 */
public class ReactiveDownloadConcept extends AbstractDownloadConcept {

    public ReactiveDownloadConcept(DownloadContextFactory contextFactory, List<DownloadHandler> handlers, DownloadEventPublisher eventPublisher, DownloadLogger logger) {
        super(contextFactory, handlers, eventPublisher, logger);
    }

    @Override
    protected Mono<?> doDownload(DownloadContext context,
                                 List<DownloadHandler> handlers,
                                 Runnable onSuccess,
                                 Consumer<Throwable> onError,
                                 Runnable onComplete) {
        Object object = new ReactiveDownloadHandlerChain(0, handlers).next(context);
        Mono<?> mono = Mono.from((Mono<?>) object)
                .doOnSuccess(it -> onSuccess.run())
                .doOnError(onError)
                .doAfterTerminate(onComplete);
        DownloadOptions options = DownloadOptions.getOptions(context);
        Executor executor = DownloadExecutor.getExecutor(context);
        if (options.getAsyncConsumer() != null && executor != null) {
            executor.execute(mono::subscribe);
            return Mono.empty();
        } else {
            return mono;
        }
    }
}
