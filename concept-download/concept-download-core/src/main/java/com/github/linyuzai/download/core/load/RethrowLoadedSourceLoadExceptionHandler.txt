package com.github.linyuzai.download.core.load;

import java.util.Collection;

/**
 * 加载结束时，抛出异常 / An exception is thrown at the end of loading
 */
public class RethrowLoadedSourceLoadExceptionHandler implements SourceLoadExceptionHandler {

    /**
     * 忽略加载过程中的异常 / Ignore exceptions during loading
     *
     * @param e 异常 / exception
     */
    @Override
    public void onLoading(SourceLoadException e) {

    }

    /**
     * 抛出第一个异常 / Throw the first exception
     *
     * @param exceptions 一个或多个异常 / One or more exceptions
     */
    @Override
    public void onLoaded(Collection<SourceLoadException> exceptions) {
        throw exceptions.iterator().next();
    }
}
