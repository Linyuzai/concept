package com.github.linyuzai.cloud.web.core.intercept.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BreakIntercept {

    boolean useResponseBodyAsWebResult() default true;
}
