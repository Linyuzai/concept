package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.executor.DownloadExecutor;
import com.github.linyuzai.download.core.source.Source;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.concurrent.Executor;

@Deprecated
public interface ReactorSource extends Source {

    Mono<Void> preload(DownloadContext context);

    static Mono<Object> preload(DownloadContext context, Collection<? extends ReactorSource> sources) {
        Executor executor = DownloadExecutor.getExecutor(context);
        return Flux.fromIterable(sources)
                .parallel()
                .runOn(executor == null ? Schedulers.immediate() : Schedulers.fromExecutor(executor))
                .flatMap(it -> it.preload(context))
                //.runOn(Schedulers.immediate())
                .sequential()
                .collectList()
                .map(it -> it);
    }
}
