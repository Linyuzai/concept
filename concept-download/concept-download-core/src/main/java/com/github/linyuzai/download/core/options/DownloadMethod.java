package com.github.linyuzai.download.core.options;

import lombok.Builder;
import lombok.Value;

import java.lang.reflect.Method;

/**
 * 下载方法，主要用于切面记录对应的方法。
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
