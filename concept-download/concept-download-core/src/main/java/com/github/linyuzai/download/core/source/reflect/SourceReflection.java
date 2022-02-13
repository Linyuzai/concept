package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 基础注解，方便扩展自定义注解。
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceReflection {

    /**
     * 对应 {@link Source} 的方法名称。
     *
     * @return {@link Source} 的方法名称
     */
    String methodName() default "";

    /**
     * 对应 {@link Source} 的方法参数类型。
     *
     * @return {@link Source} 的方法参数类型
     */
    Class<?> methodParameterType() default void.class;

    /**
     * 对应 {@link Source} 的字段名称。
     *
     * @return {@link Source} 的字段名称
     */
    String fieldName() default "";
}
