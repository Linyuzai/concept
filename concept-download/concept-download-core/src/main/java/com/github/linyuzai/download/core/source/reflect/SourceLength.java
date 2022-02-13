package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 标记一个字段或方法作为 {@link Source} 长度的值。
 *
 * @see Source#getLength()
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setLength", methodParameterType = Long.class, fieldName = "length")
public @interface SourceLength {

}
