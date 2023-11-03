package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;

public interface ReactorSource extends Source {

    Mono<Void> preload(DownloadContext context);

    @SneakyThrows
    static void preload(DownloadContext context, Collection<? extends ReactorSource> sources) {
        Flux.fromIterable(sources).parallel()
                .runOn(Schedulers.fromExecutor(null))
                .flatMap(it -> it.preload(context))
                //.runOn(Schedulers.immediate())
                .sequential()
                .collectList()
                .toFuture()
                .get();
    }
}
