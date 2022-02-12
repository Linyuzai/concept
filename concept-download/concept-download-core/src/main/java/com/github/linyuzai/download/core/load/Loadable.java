package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import reactor.core.publisher.Mono;

/**
 * 支持 {@link Source} 的预加载。
 */
public interface Loadable extends Downloadable {

    /**
     * 加载。
     *
     * @param context {@link DownloadContext}
     */
    Mono<Source> load(DownloadContext context);

    /**
     * 异步加载会通过多线程等方式处理。
     *
     * @return 是否异步加载
     */
    boolean isAsyncLoad();
}
