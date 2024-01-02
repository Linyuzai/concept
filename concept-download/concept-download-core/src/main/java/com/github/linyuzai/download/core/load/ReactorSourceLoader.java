package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.reactive.ReactorSource;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Deprecated
public class ReactorSourceLoader extends CompletableFutureSourceLoader {

    @SneakyThrows
    @Override
    public void concurrentLoad(Collection<Source> sources, DownloadContext context) {
        List<ReactorSource> reactorSources = new ArrayList<>();
        List<Source> others = new ArrayList<>();
        for (Source source : sources) {
            if (source instanceof ReactorSource) {
                reactorSources.add((ReactorSource) source);
            } else {
                others.add(source);
            }
        }
        if (!reactorSources.isEmpty() && !others.isEmpty()) {
            CompletableFuture<Void> reactorFuture = CompletableFuture.runAsync(() ->
                    ReactorSource.preload(context, reactorSources));
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                    super.concurrentLoad(sources, context));
            CompletableFuture.allOf(reactorFuture, future).get();
        } else if (!reactorSources.isEmpty()) {
            ReactorSource.preload(context, reactorSources);
            for (ReactorSource reactorSource : reactorSources) {
                reactorSource.load(context);
            }
        } else if (!others.isEmpty()) {
            super.concurrentLoad(sources, context);
        }
    }
}
