package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;

/**
 * 插件过滤器
 */
public interface PluginFilter {

    /**
     * 过滤插件
     *
     * @param context 上下文 {@link PluginContext}
     */
    void filter(PluginContext context);

    /**
     * 取反
     *
     * @return {@link PluginFilter} 本身
     */
    PluginFilter negate();

    /**
     * 获得插件过滤器 {@link PluginFilter} 需要依赖的插件处理器 {@link PluginResolver}。
     * 默认通过类上标注的注解 {@link FilterWithResolver} 获得。
     *
     * @return 所需要依赖的插件处理器 {@link PluginResolver} 的类
     */
    default Class<? extends PluginResolver> filterWith() {
        Class<?> clazz = getClass();
        while (clazz != null) {
            FilterWithResolver annotation = clazz.getAnnotation(FilterWithResolver.class);
            if (annotation != null) {
                return annotation.value();
            }
            clazz = clazz.getSuperclass();
        }
        throw new PluginException("A plugin resolve must be bound");
    }
}
