package com.github.linyuzai.plugin.core.handle.extract.match;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定插件内容编码格式
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginText {

    /**
     * 编码
     *
     * @return 编码
     */
    String charset() default "";
}
