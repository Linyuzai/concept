package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.multiple.MultipleSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerSourceLoader extends ConcurrentSourceLoader {

    private Scheduler scheduler;

    @Override
    public Mono<Source> concurrentLoad(Collection<Source> sources, DownloadContext context) {
        List<Mono<Source>> monoList = sources.stream()
                .map(it -> Mono.just(it)
                        .publishOn(getScheduler())
                        .flatMap(s -> s.load(context)))
                .collect(Collectors.toList());
        List<Source> block = Mono.zip(monoList, objects -> Arrays.stream(objects)
                        .map(Source.class::cast)
                        .collect(Collectors.toList()))
                .block();
        return Mono.just(new MultipleSource(block == null ? Collections.emptyList() : block));
    }
}
