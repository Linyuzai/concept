package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ParallelOriginalSourceLoader implements OriginalSourceLoader {

    @Getter
    @Setter
    private boolean serialOnSingle = true;

    @Override
    public void load(OriginalSource source, DownloadContext context) throws IOException {
        Collection<OriginalSource> sources = source.flatten();
        if (sources.size() <= 1 && serialOnSingle) {
            source.load();
            return;
        }
        Collection<OriginalSource> parallelSources = new ArrayList<>();
        Collection<OriginalSource> serialSources = new ArrayList<>();
        for (OriginalSource s : sources) {
            if (s.isAsyncLoad()) {
                parallelSources.add(s);
            } else {
                serialSources.add(s);
            }
        }
        if (!parallelSources.isEmpty()) {
            parallelLoad(parallelSources, context);
        }
        if (!serialSources.isEmpty()) {
            for (OriginalSource serialSource : serialSources) {
                serialSource.load();
            }
        }
    }

    public void parallelLoad(Collection<OriginalSource> sources, DownloadContext context) {
        CountDownLatch latch = new CountDownLatch(sources.size());
        List<Throwable> throwableList = Collections.synchronizedList(new ArrayList<>());
        for (OriginalSource source : sources) {
            execute(() -> {
                try {
                    source.load();
                } catch (IOException e) {
                    throwableList.add(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throwableList.add(e);
        }
        if (!throwableList.isEmpty()) {
            handleThrowableList(throwableList, context);
        }
    }

    @SneakyThrows
    public void handleThrowableList(Collection<Throwable> throwableList, DownloadContext context) {
        throw throwableList.iterator().next();
    }

    public abstract void execute(Runnable runnable);
}
