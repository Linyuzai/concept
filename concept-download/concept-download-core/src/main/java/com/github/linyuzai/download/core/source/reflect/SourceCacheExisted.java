package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * @see Source#isCacheExisted()
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setCacheExisted", methodParameterType = boolean.class, fieldName = "cacheExisted")
public @interface SourceCacheExisted {

}
