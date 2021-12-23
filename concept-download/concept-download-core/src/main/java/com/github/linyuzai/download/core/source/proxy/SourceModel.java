package com.github.linyuzai.download.core.source.proxy;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SourceProxy
public @interface SourceModel {

    boolean superclass() default true;
}
