package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public interface PluginHandler {

    /**
     * 处理
     *
     * @param context 上下文 {@link PluginContext}
     */
    void handle(PluginContext context);

    /**
     * 是否支持
     *
     * @param context 上下文 {@link PluginContext}
     * @return 如果支持返回 true 否则返回 false
     */
    boolean support(PluginContext context);

    interface Dependency<D extends PluginHandler> {

        /**
         * 获得依赖的处理器类。
         *
         * @return 依赖的处理器类
         */
        @SuppressWarnings("unchecked")
        default Collection<Class<? extends D>> getDependencies() {
            HandlerDependency annotation = ReflectionUtils.findAnnotation(getClass(), HandlerDependency.class);
            if (annotation != null) {
                return Arrays.stream(annotation.value())
                        .map(it -> (Class<D>) it)
                        .collect(Collectors.toList());
            }
            return null;
        }
    }
}
