package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
public abstract class AsyncSourceLoader implements SourceLoader {

    @Getter
    @Setter
    private boolean serialOnSingle = true;

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
        Collection<Mono<Source>> monoList = new ArrayList<>();
        if (!asyncSources.isEmpty()) {
            if (asyncSources.size() == 1 && serialOnSingle) {
                for (Source s : asyncSources) {
                    monoList.add(s.load(context));
                }
            } else {
                monoList.add(loadAsync(asyncSources, context));
            }
        }
        List<Mono<Source>> syncMonoList = syncSources.stream()
                .map(it -> it.load(context))
                .collect(Collectors.toList());
        monoList.addAll(syncMonoList);
        return Mono.zip(monoList, objects -> objects).map(it -> source);
    }

    public abstract Mono<Source> loadAsync(Collection<Source> sources, DownloadContext context);
}
