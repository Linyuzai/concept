package com.github.linyuzai.download.core.load.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.reactive.ReactorSource;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Deprecated
public interface ReactiveSourceLoader extends SourceLoader {

    default Object load(Source source, DownloadContext context, Supplier<?> supplier) {
        List<ReactorSource> reactorSources = new ArrayList<>();
        Collection<Source> sources = source.list();
        for (Source s : sources) {
            if (s instanceof ReactorSource) {
                reactorSources.add((ReactorSource) s);
            }
        }
        if (reactorSources.isEmpty()) {
            load(source, context);
            return supplier.get();
        } else {
            Mono<Object> preload = ReactorSource.preload(context, reactorSources);
            return preload
                    //.doOnSuccess(it -> load(source, context))
                    .flatMap(it -> {
                        load(source, context);
                        Object o = supplier.get();
                        if (o instanceof Publisher) {
                            return Mono.from((Publisher<? extends Void>) o);
                        } else {
                            return Mono.justOrEmpty(o);
                        }
                    });
        }
    }
}
