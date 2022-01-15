package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.multiple.MultipleSource;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BlockSourceLoader extends AsyncSourceLoader {

    @Override
    public Mono<Source> loadAsync(Collection<Source> sources, DownloadContext context) {
        List<Mono<Source>> monoList = sources.stream()
                .map(it -> Mono.just(it)
                        .publishOn(getScheduler(context))
                        .flatMap(s -> s.load(context)))
                .collect(Collectors.toList());
        List<Source> result = new ArrayList<>();
        Disposable disposable = Mono.zip(monoList, objects -> Arrays.stream(objects)
                        .map(Source.class::cast)
                        .collect(Collectors.toList()))
                .subscribe(result::addAll, e -> {

                });

        return Mono.just(new MultipleSource(result))
                .doOnCancel(disposable::dispose)
                .map(Source.class::cast);
    }

    public abstract Scheduler getScheduler(DownloadContext context);
}
