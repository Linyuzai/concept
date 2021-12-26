package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setCacheEnabled", methodParameterType = boolean.class, fieldName = "cacheEnabled")
public @interface SourceCacheEnabled {

}
