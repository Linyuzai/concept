package com.github.linyuzai.plugin.core.match;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定匹配 {@link java.util.Properties} 或是具体的属性
 */
@Deprecated
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface PluginProperties {

    /**
     * 属性名
     *
     * @return 属性名
     */
    String[] value() default {};
}
