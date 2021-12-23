package com.github.linyuzai.download.core.source.reflection;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SourceReflection
public @interface SourceModel {

    boolean superclass() default true;
}
