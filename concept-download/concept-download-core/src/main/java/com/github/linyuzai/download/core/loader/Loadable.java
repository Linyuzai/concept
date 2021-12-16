package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.concept.Downloadable;

import java.io.IOException;

/**
 * 可加载的，用于支持预加载 / Loadable to support preloading
 */
public interface Loadable extends Downloadable {

    /**
     * 执行预加载 / Perform preload
     *
     * @param context 下载上下文 / Context of download
     */
    default void load(DownloadContext context) {

    }

    /**
     * 如果异步加载，会通过多线程等方式处理 / If it is loaded asynchronously, it will be processed through multithreading
     *
     * @return 是否异步加载 / If async load
     */
    boolean isAsyncLoad();
}
