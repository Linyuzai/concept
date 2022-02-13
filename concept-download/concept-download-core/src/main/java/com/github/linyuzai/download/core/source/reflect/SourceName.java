package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.source.Source;

import java.lang.annotation.*;

/**
 * 标记一个字段或方法作为 {@link Source} 名称的值。
 *
 * @see Source#getName()
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SourceReflection(methodName = "setName", methodParameterType = String.class, fieldName = "name")
public @interface SourceName {

}
