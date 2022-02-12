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

/**
 * {@link SourceLoader} 的默认实现。
 * 其中 {@link Scheduler} 的设置将会忽略 {@link Source#isAsyncLoad()}，
 * 如不想忽略可使用 {@link SchedulerSourceLoader}。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultSourceLoader implements SourceLoader {

    /**
     * Reactor调度器
     */
    private Scheduler scheduler = Schedulers.immediate();

    /**
     * 使用 {@link Flux#fromIterable(Iterable)} 在指定的 {@link Scheduler} 上加载。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    @Override
    public Mono<Source> load(Source source, DownloadContext context) {
        return Flux.fromIterable(source.list())
                .publishOn(scheduler)
                .flatMap(it -> it.load(context))
                .collectList()
                .map(MultipleSource::new);
    }
}
