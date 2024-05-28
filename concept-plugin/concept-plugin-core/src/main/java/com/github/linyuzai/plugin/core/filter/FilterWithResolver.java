package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.resolve.PluginResolver;

import java.lang.annotation.*;

/**
 * 标记在 {@link PluginFilter} 上用于指定过滤器对应的插件解析器 {@link PluginResolver}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface FilterWithResolver {

    Class<? extends PluginResolver> value();
}
