package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.*;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

@NoArgsConstructor
public abstract class ConcurrentSourceLoader extends ParallelSourceLoader {

    public ConcurrentSourceLoader(boolean serialOnSingle) {
        super(serialOnSingle);
    }

    @SneakyThrows
    @Override
    public void parallelLoad(Collection<Source> sources, DownloadContext context) {
        CountDownLatch latch = new CountDownLatch(sources.size());
        for (Source source : sources) {
            execute(() -> {
                try {
                    source.load(context);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    public abstract void execute(Runnable runnable) throws IOException;
}
