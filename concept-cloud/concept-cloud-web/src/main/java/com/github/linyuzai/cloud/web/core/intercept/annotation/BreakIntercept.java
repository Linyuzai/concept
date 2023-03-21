package com.github.linyuzai.cloud.web.core.intercept.annotation;

import java.lang.annotation.*;

/**
 * 中断拦截，基于断言
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BreakIntercept {

    /**
     * 是否使用原始响应体作为返回值
     * <p>
     * 中断拦截后，导致 WebResult 为 null
     * <p>
     * 设置该值将使用原始的响应体作为返回值
     */
    boolean useResponseBodyAsWebResult() default true;
}
