package com.github.linyuzai.download.core.load;

import java.util.Collection;

/**
 * 下载源加载的异常处理器 / Handler to handle exception when source loading
 */
public interface SourceLoadExceptionHandler {

    /**
     * 每个异常都会回调 / Each exception will be called back
     * 可能是在线程池中的某个线程中回调 / It may be a callback in a thread in the thread pool
     *
     * @param e 异常 / exception
     */
    void onLoading(SourceLoadException e);

    /**
     * 加载结束后，如果有异常将会回调 / If there is any exception, it will be called back after loading
     * 会在触发下载的主线程回调 / Will call back in the main thread of the download
     *
     * @param exceptions 一个或多个异常 / One or more exceptions
     */
    void onLoaded(Collection<SourceLoadException> exceptions);
}
