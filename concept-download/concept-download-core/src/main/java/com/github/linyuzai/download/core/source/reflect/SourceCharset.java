package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;
import java.nio.charset.Charset;

/**
 * 标记一个字段或方法作为 {@link Source} 的 {@link Charset} 的值。
 *
 * @see Source#getCharset()
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setCharset", methodParameterType = Charset.class, fieldName = "charset")
public @interface SourceCharset {

}
