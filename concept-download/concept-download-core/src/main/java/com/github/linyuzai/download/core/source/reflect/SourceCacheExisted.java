package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 标记一个字段或方法作为 {@link Source} 缓存是否存在的值。
 *
 * @see Source#isCacheExisted()
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setCacheExisted", methodParameterType = boolean.class, fieldName = "cacheExisted")
public @interface SourceCacheExisted {

}
