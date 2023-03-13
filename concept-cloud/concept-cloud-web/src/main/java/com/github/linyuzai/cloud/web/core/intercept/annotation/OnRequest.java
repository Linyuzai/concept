package com.github.linyuzai.cloud.web.core.intercept.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnRequest {

    Class<?> value() default void.class;
}