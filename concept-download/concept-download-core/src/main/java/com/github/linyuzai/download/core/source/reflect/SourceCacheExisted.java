package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setCacheExisted", methodParameterType = boolean.class, fieldName = "cacheExisted")
public @interface SourceCacheExisted {

}
