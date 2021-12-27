package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;
import java.nio.charset.Charset;

/**
 * @see Source#getCharset()
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setCharset", methodParameterType = Charset.class, fieldName = "charset")
public @interface SourceCharset {

}
