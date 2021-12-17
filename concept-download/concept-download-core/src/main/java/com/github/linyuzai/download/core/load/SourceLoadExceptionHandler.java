package com.github.linyuzai.download.core.load;

import java.util.Collection;

/**
 * 下载源加载的异常处理器 / Handler to handle exception when source loading
 */
public interface SourceLoadExceptionHandler {

    /**
     * 每个异常都会回调 / Each exception will be called back
     *
     * @param e 异常 / exception
     */
    void onLoading(SourceLoadException e);

    /**
     * 加载结束后，如果有异常将会回调 / If there is any exception, it will be called back after loading
     *
     * @param exceptions 一个或多个异常 / One or more exceptions
     */
    void onLoaded(Collection<SourceLoadException> exceptions);
}
