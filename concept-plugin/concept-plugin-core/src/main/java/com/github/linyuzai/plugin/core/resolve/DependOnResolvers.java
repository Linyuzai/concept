package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.match.PluginMatcher;

import java.lang.annotation.*;

/**
 * 标记在插件解析器 {@link PluginResolver} 和插件匹配器 {@link PluginMatcher} 上，
 * 指定需要依赖的插件解析器 {@link PluginResolver}。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface DependOnResolvers {

    /**
     * 需要依赖的插件解析器 {@link PluginResolver}
     *
     * @return 需要依赖的插件解析器 {@link PluginResolver}
     */
    Class<PluginResolver>[] value() default {};
}
