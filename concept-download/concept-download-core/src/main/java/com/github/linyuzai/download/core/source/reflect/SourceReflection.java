package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.reflect.conversion.ValueConvertor;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection
public @interface SourceReflection {

    String methodName() default "";

    Class<?> methodParameterType() default void.class;

    String fieldName() default "";

    Class<? extends ValueConvertor> convertor() default ValueConvertor.class;
}
