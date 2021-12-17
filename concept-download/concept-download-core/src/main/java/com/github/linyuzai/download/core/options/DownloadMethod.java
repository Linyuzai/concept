package com.github.linyuzai.download.core.options;

import lombok.Builder;
import lombok.Value;

import java.lang.reflect.Method;

/**
 * 下载方法，主要用于切面 / Download method, mainly used for aop
 */
@Value
@Builder(builderClassName = "Builder")
public class DownloadMethod {

    /**
     * 方法 / Method
     */
    Method method;

    /**
     * 入参 / Parameters
     */
    Object[] parameters;

    /**
     * 返回值 / Return value
     */
    Object returnValue;
}
