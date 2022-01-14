package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * 串行的调用器 / Serial loader invoker
 */
public class SerialSourceLoaderInvoker implements SourceLoaderInvoker {

    /**
     * 按顺序调用 / Invoke in order
     *
     * @param loaders 加载器 / Loaders
     * @param context 下载上下文 / Context of download
     * @return 加载结果 / Results of loadings
     */
    @Override
    public Mono<? extends Collection<? extends SourceLoadResult>> invoke(Collection<? extends SourceLoader> loaders, DownloadContext context) {
        return Flux.fromIterable(loaders)
                .flatMap(it -> it.load(context))
                .collectList();
    }
}
