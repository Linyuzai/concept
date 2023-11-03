package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.executor.DownloadExecutor;
import com.github.linyuzai.download.core.source.Source;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CompletableFutureSourceLoader extends ConcurrentSourceLoader {

    @SneakyThrows
    @Override
    public void concurrentLoad(Collection<Source> sources, DownloadContext context) {
        Executor executor = DownloadExecutor.getExecutor(context);
        CompletableFuture<?>[] futures;
        if (executor == null) {
            futures = sources.stream()
                    .map(it -> CompletableFuture.runAsync(() -> it.load(context)))
                    .toArray(CompletableFuture[]::new);
        } else {
            futures = sources.stream()
                    .map(it -> CompletableFuture.runAsync(() -> it.load(context), executor))
                    .toArray(CompletableFuture[]::new);
        }
        CompletableFuture.allOf(futures).get();
    }
}
