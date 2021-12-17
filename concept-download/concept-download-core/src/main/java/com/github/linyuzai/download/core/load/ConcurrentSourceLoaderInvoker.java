package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

@NoArgsConstructor
public abstract class ConcurrentSourceLoaderInvoker extends ParallelSourceLoaderInvoker {

    public ConcurrentSourceLoaderInvoker(boolean serialOnSingle) {
        super(serialOnSingle);
    }

    @SneakyThrows
    @Override
    public Collection<SourceLoadResult> parallelInvoke(Collection<SourceLoader> loaders, DownloadContext context) {
        CountDownLatch latch = new CountDownLatch(loaders.size());
        Collection<SourceLoadResult> results = Collections.synchronizedList(new ArrayList<>());
        for (SourceLoader loader : loaders) {
            execute(() -> {
                try {
                    SourceLoadResult result = loader.load(context);
                    results.add(result);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        return results;
    }

    public abstract void execute(Runnable runnable) throws IOException;
}
