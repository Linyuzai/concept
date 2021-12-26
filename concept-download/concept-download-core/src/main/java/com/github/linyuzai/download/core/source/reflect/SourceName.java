package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setName", methodParameterType = String.class, fieldName = "name")
public @interface SourceName {

}
