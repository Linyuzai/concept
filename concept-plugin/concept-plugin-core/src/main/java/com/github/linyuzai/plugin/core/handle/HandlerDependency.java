package com.github.linyuzai.plugin.core.handle;

import java.lang.annotation.*;

/**
 * 标记在 {@link PluginHandler} 上用于指定依赖的插件处理器
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandlerDependency {

    /**
     * 依赖的插件处理器
     */
    Class<? extends PluginHandler>[] value();
}
