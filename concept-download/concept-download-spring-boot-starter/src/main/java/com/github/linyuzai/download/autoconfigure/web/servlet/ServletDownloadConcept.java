package com.github.linyuzai.download.autoconfigure.web.servlet;

import com.github.linyuzai.download.core.concept.AbstractDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadMode;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.executor.DownloadExecutor;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.logger.DownloadLogger;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 基于 {@link DownloadHandlerChain} 的 {@link DownloadConcept} 实现。
 */
public class ServletDownloadConcept extends AbstractDownloadConcept {

    public ServletDownloadConcept(DownloadContextFactory contextFactory, List<DownloadHandler> handlers, DownloadEventPublisher eventPublisher, DownloadLogger logger) {
        super(contextFactory, handlers, eventPublisher, logger);
    }

    @Override
    protected Object doDownload(DownloadContext context,
                                List<DownloadHandler> handlers,
                                Runnable onSuccess,
                                Consumer<Throwable> onError,
                                Runnable onComplete) {
        Supplier<?> supplier = () -> {
            try {
                Object next = new ServletDownloadHandlerChain(handlers).next(context);
                onSuccess.run();
                return next;
            } catch (Throwable e) {
                onError.accept(e);
                return null;
            } finally {
                onComplete.run();
            }
        };
        Executor executor = DownloadExecutor.getExecutor(context);
        if (DownloadMode.getMode(context) == DownloadMode.ASYNC) {
            if (executor == null) {
                new Thread(supplier::get).start();
            } else {
                executor.execute(supplier::get);
            }
            return null;
        } else {
            return supplier.get();
        }
    }
}
