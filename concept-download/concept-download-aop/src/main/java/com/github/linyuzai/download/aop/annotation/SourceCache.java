package com.github.linyuzai.download.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceCache {

    boolean enabled() default true;

    String group() default "";

    boolean delete() default false;
}
