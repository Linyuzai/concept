package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ParallelDownloadSourceLoader implements DownloadSourceLoader {

    @Getter
    @Setter
    private boolean serialOnSingleSource = true;

    @Override
    public void load(DownloadSource source, DownloadContext context) throws IOException {
        Collection<DownloadSource> sources = source.flatten();
        if (sources.size() <= 1 && serialOnSingleSource) {
            source.load();
            return;
        }
        Collection<DownloadSource> parallelSources = new ArrayList<>();
        Collection<DownloadSource> serialSources = new ArrayList<>();
        for (DownloadSource s : sources) {
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
            for (DownloadSource serialSource : serialSources) {
                serialSource.load();
            }
        }
    }

    public void parallelLoad(Collection<DownloadSource> sources, DownloadContext context) {
        CountDownLatch latch = new CountDownLatch(sources.size());
        List<Throwable> throwableList = Collections.synchronizedList(new ArrayList<>());
        for (DownloadSource source : sources) {
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
