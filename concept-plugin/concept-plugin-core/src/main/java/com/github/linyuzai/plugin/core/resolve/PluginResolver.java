package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件解析器
 */
public interface PluginResolver extends PluginResolverDependency {

    /**
     * 解析
     *
     * @param context 上下文 {@link PluginContext}
     */
    void resolve(PluginContext context);

    /**
     * 是否支持解析
     *
     * @param context 上下文 {@link PluginContext}
     * @return 如果支持解析返回 true 否则返回 false
     */
    boolean support(PluginContext context);
}
