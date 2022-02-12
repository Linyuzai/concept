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

import java.util.*;

/**
 * 支持并发的 {@link SourceLoader}。
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class ConcurrentSourceLoader implements SourceLoader {

    /**
     * 单个文件的时候串行加载
     */
    @Getter
    @Setter
    private boolean serialOnSingle = true;

    /**
     * 将 {@link Source} 中需要异步加载的部分进行并发加载，
     * 并与同步加载之后的 {@link Source} 合并。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    @Override
    public Mono<Source> load(Source source, DownloadContext context) {
        Collection<Source> syncSources = new ArrayList<>();
        Collection<Source> asyncSources = new ArrayList<>();
        Collection<Source> sources = source.list();
        for (Source s : sources) {
            if (s.isAsyncLoad()) {
                asyncSources.add(s);
            } else {
                syncSources.add(s);
            }
        }

        if (asyncSources.isEmpty()) {
            return Flux.fromIterable(syncSources)
                    .flatMap(it -> it.load(context))
                    .collectList()
                    .map(MultipleSource::new);
        } else {
            if (asyncSources.size() == 1 && serialOnSingle) {
                syncSources.add(asyncSources.iterator().next());
                return Flux.fromIterable(syncSources)
                        .flatMap(it -> it.load(context))
                        .collectList()
                        .map(MultipleSource::new);
            } else {
                Mono<Source> syncMono = Flux.defer(() -> Flux.fromIterable(syncSources))
                        .flatMap(it -> it.load(context))
                        .collectList()
                        .map(MultipleSource::new);
                Mono<Source> asyncMono = Mono.defer(() -> concurrentLoad(asyncSources, context));
                return Mono.zip(syncMono, asyncMono).map(objects -> {
                    Collection<Source> newSources = new ArrayList<>();
                    newSources.addAll(objects.getT1().list());
                    newSources.addAll(objects.getT2().list());
                    return new MultipleSource(newSources);
                });
            }
        }
    }


    /**
     * 并发加载。
     *
     * @param sources {@link Source} 集合
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    public abstract Mono<Source> concurrentLoad(Collection<Source> sources, DownloadContext context);
}
