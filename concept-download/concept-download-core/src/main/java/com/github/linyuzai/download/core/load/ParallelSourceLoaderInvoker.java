package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 并行的调用器 / Parallel loader invoker
 */
@AllArgsConstructor
@NoArgsConstructor
public abstract class ParallelSourceLoaderInvoker implements SourceLoaderInvoker {

    /**
     * 只有一个下载器时，是否串行执行，默认true / Whether to invoke serially when there is only one loader. The default is true
     */
    @Getter
    @Setter
    private boolean serialOnSingle = true;

    /**
     * 将同步加载和异步加载的加载器分开 / Separate loaders for synchronous and asynchronous loading
     * 同步加载器直接串行加载 / Synchronous loader invoke serial
     * 异步加载器进行并行加载 / Asynchronous loader invoke parallel
     *
     * @param loaders 加载器 / Loaders
     * @param context 下载上下文 / Context of download
     * @return 加载结果 / Results of loadings
     * @throws IOException I/O exception
     */
    @Override
    public Collection<SourceLoadResult> invoke(Collection<? extends SourceLoader> loaders, DownloadContext context) {
        Collection<SourceLoader> parallelSourceLoaders = new ArrayList<>();
        Collection<SourceLoader> serialSourceLoaders = new ArrayList<>();
        for (SourceLoader loader : loaders) {
            if (loader.isAsyncLoad()) {
                parallelSourceLoaders.add(loader);
            } else {
                serialSourceLoaders.add(loader);
            }
        }
        Collection<SourceLoadResult> results = new ArrayList<>();
        if (!parallelSourceLoaders.isEmpty()) {
            if (parallelSourceLoaders.size() == 1 && serialOnSingle) {
                for (SourceLoader loader : parallelSourceLoaders) {
                    SourceLoadResult result = loader.load(context);
                    results.add(result);
                }
            } else {
                results.addAll(parallelInvoke(parallelSourceLoaders, context));
            }
        }
        if (!serialSourceLoaders.isEmpty()) {
            for (SourceLoader loader : serialSourceLoaders) {
                SourceLoadResult result = loader.load(context);
                results.add(result);
            }
        }
        return results;
    }

    /**
     * 并行调用 / Invoke parallel
     *
     * @param loaders 加载器 / Loaders
     * @param context 下载上下文 / Context of download
     * @return 加载结果 / Results of loadings
     */
    public abstract Collection<SourceLoadResult> parallelInvoke(Collection<SourceLoader> loaders, DownloadContext context);
}
