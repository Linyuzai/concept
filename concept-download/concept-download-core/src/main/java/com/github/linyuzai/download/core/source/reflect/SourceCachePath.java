package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 标记一个字段或方法作为 {@link Source} 缓存路径的值。
 *
 * @see Source#getCachePath()
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setCachePath", methodParameterType = String.class, fieldName = "cachePath")
public @interface SourceCachePath {

}
