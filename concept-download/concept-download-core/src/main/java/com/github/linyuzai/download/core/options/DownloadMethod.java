package com.github.linyuzai.download.core.options;

import lombok.Builder;
import lombok.Value;

import java.lang.reflect.Method;

/**
 * 下载方法，切面中拦截的方法。
 */
@Value
@Builder(builderClassName = "Builder")
public class DownloadMethod {

    /**
     * 方法
     */
    Method method;

    /**
     * 入参
     */
    Object[] parameters;

    /**
     * 返回值
     */
    Object returnValue;
}
