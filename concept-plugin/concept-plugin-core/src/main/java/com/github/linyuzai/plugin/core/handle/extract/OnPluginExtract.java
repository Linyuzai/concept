package com.github.linyuzai.plugin.core.handle.extract;

import java.lang.annotation.*;

/**
 * 标记一个方法作为插件回调的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnPluginExtract {
}
