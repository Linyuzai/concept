package com.github.linyuzai.download.core.source.reflect;

import java.lang.annotation.*;
import java.nio.charset.Charset;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setCharset", methodParameterType = Charset.class, fieldName = "charset")
public @interface SourceCharset {

}
