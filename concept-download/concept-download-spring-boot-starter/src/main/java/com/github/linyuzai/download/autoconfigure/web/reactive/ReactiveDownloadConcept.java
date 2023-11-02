package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.concept.AbstractDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 基于 {@link DownloadHandlerChain} 的 {@link DownloadConcept} 实现。
 */
public class ReactiveDownloadConcept extends AbstractDownloadConcept {

    public ReactiveDownloadConcept(DownloadContextFactory contextFactory, List<DownloadHandler> handlers) {
        super(contextFactory, handlers);
    }

    @Override
    protected Object doDownload(DownloadContext context, List<DownloadHandler> handlers, Runnable onComplete) {
        Mono<Void> mono = (Mono<Void>) new ReactiveDownloadHandlerChain(0, handlers).next(context);
        //最后销毁上下文
        mono.doAfterTerminate(onComplete);
        return mono;
    }
}
