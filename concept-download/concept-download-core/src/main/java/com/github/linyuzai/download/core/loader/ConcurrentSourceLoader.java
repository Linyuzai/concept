package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ConcurrentSourceLoader extends ParallelSourceLoader {

    @Override
    public void parallelLoad(Collection<Source> sources, DownloadContext context) {
        CountDownLatch latch = new CountDownLatch(sources.size());
        List<Throwable> throwableList = Collections.synchronizedList(new ArrayList<>());
        for (Source source : sources) {
            execute(() -> {
                try {
                    source.load(context);
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
