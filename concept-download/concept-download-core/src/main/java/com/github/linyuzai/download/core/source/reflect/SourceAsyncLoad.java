package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * @see Source#isAsyncLoad()
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setAsyncLoad", methodParameterType = boolean.class, fieldName = "asyncLoad")
public @interface SourceAsyncLoad {

}
