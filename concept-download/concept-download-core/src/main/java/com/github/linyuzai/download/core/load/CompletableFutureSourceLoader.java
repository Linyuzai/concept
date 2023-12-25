package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.executor.DownloadExecutor;
import com.github.linyuzai.download.core.source.Source;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class CompletableFutureSourceLoader extends ConcurrentSourceLoader {

    @SneakyThrows
    @Override
    public void concurrentLoad(Collection<Source> sources, DownloadContext context) {
        Executor executor = DownloadExecutor.getExecutor(context);
        Function<Source, CompletableFuture<?>> function;
        if (executor == null) {
            function = it -> CompletableFuture.runAsync(() -> it.load(context));
        } else {
            function = it -> CompletableFuture.runAsync(() -> it.load(context), executor);
        }
        CompletableFuture<?>[] futures = sources.stream()
                .map(function)
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).get();
    }
}
