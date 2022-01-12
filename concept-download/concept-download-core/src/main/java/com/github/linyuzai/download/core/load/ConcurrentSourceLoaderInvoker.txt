package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * 并发调用的调用器 / Invokers for concurrent invokes
 */
@NoArgsConstructor
public abstract class ConcurrentSourceLoaderInvoker extends ParallelSourceLoaderInvoker {

    /**
     * @param serialOnSingle 只有一个下载器时，是否串行执行，默认true / Whether to invoke serially when there is only one loader. The default is true
     */
    public ConcurrentSourceLoaderInvoker(boolean serialOnSingle) {
        super(serialOnSingle);
    }

    /**
     * 通过{@link CountDownLatch} 实现 / Implemented by {@link CountDownLatch}
     *
     * @param loaders 加载器 / Loaders
     * @param context 下载上下文 / Context of download
     * @return 加载结果 / Results of loadings
     */
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

    /**
     * 执行操作 / Perform execute
     *
     * @param runnable {@link Runnable}
     * @throws IOException I/O exception
     */
    public abstract void execute(Runnable runnable) throws IOException;
}
