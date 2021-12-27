package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

/**
 * 基础注解 / Basic annotation
 * 方法反射优先于字段反射 / Method reflection takes precedence over field reflection
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceReflection {

    /**
     * @return 对应下载源的方法名称 / Method name corresponding to the source
     */
    String methodName() default "";

    /**
     * @return 对应下载源的方法参数类型 / The method parameter type corresponding to the source
     */
    Class<?> methodParameterType() default void.class;

    /**
     * @return 对应下载源的字段 / Fields corresponding to the download source
     */
    String fieldName() default "";
}
