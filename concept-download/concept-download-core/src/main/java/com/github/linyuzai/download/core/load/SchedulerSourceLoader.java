package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.multiple.MultipleSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;

/**
 * 支持 {@link Scheduler} 的 {@link SourceLoader}。
 * 如果想要忽略 {@link Source#isAsyncLoad()}
 * 可以使用 {@link DefaultSourceLoader}。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerSourceLoader extends ConcurrentSourceLoader {

    /**
     * Reactor调度器
     */
    private Scheduler scheduler = Schedulers.immediate();

    /**
     * 并发加载使用指定的 {@link Scheduler}。
     *
     * @param sources {@link Source} 集合
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    @Override
    public Mono<Source> concurrentLoad(Collection<Source> sources, DownloadContext context) {
        return Flux.fromIterable(sources)
                .publishOn(scheduler)
                .flatMap(it -> it.load(context))
                .collectList()
                .map(MultipleSource::new);
    }
}
