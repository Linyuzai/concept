package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * {@link PluginResolver} 抽象类
 *
 * @param <T> 未解析的插件类型
 * @param <R> 解析后的插件类型
 */
public abstract class AbstractPluginResolver<T, R> implements PluginResolver {

    @Override
    public void resolve(PluginContext context) {
        T plugin = context.get(getKey());
        R resolve = doResolve(plugin, context);
        context.set(getResolveKey(), resolve);
    }

    /**
     * 判断上下文 {@link PluginContext} 是否存在特定 key
     *
     * @param context 上下文 {@link PluginContext}
     * @return 如果上下文中存在 key 则返回 true 否则返回 false
     */
    @Override
    public boolean support(PluginContext context) {
        return context.contains(getKey());
    }

    /**
     * 基于泛型及上下文 {@link PluginContext} 的插件解析
     *
     * @param plugin  未解析的插件
     * @param context 上下文 {@link PluginContext}
     * @return 解析后的插件
     */
    public abstract R doResolve(T plugin, PluginContext context);

    /**
     * 未解析的插件的 key
     *
     * @return 未解析的插件的 key
     */
    public abstract Object getKey();

    /**
     * 解析后插件的 key
     *
     * @return 解析后插件的 key
     */
    public abstract Object getResolveKey();
}
