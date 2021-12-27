package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceReflection {

    String methodName() default "";

    Class<?> methodParameterType() default void.class;

    String fieldName() default "";
}
