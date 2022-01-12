package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.source.Source;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

/**
 * 可加载的，用于支持预加载 / Loadable to support preloading
 */
public interface Loadable extends Downloadable {

    /**
     * 执行预加载 / Perform preload
     *
     * @param context 下载上下文 / Context of download
     */
    Mono<Source> load(DownloadContext context);

    /**
     * 如果异步加载，会通过多线程等方式处理 / If it is loaded asynchronously, it will be processed through multithreading
     *
     * @return 是否异步加载 / If async load
     */
    boolean isAsyncLoad();
}
