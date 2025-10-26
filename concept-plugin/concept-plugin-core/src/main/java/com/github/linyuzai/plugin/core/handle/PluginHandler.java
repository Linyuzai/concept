package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

/**
 * 插件处理器
 */
public interface PluginHandler {

    /**
     * 处理
     */
    void handle(PluginContext context);

    /**
     * 插件处理器依赖，用于处理插件处理器的依赖关系
     */
    interface Dependency {

        /**
         * 获得依赖的处理器类
         */
        @SuppressWarnings("unchecked")
        default Class<? extends PluginHandler>[] getDependencies() {
            HandlerDependency annotation = ReflectionUtils.findAnnotation(getClass(), HandlerDependency.class);
            if (annotation == null) {
                return new Class[0];
            }
            return annotation.value();
        }
    }
}
