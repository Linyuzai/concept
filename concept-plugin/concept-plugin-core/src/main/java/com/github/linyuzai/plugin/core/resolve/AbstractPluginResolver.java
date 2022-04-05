package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;

/**
 * {@link PluginResolver} 抽象类
 *
 * @param <T> 未解析的插件类型
 * @param <R> 解析后的插件类型
 */
public abstract class AbstractPluginResolver<T, R> implements PluginResolver {

    /**
     * 解析并发布 {@link PluginResolvedEvent} 事件
     *
     * @param context 上下文 {@link PluginContext}
     */
    @Override
    public void resolve(PluginContext context) {
        Object dependedKey = getDependedKey();
        T depended = context.get(dependedKey);
        if (depended == null) {
            throw new PluginException("No plugin can be resolved with key: " + dependedKey);
        }
        R resolved = doResolve(depended, context);
        Object resolvedKey = getResolvedKey();
        context.set(resolvedKey, resolved);
        context.publish(new PluginResolvedEvent(context, this, dependedKey, depended, resolvedKey, resolved));
    }

    /**
     * 判断上下文 {@link PluginContext} 是否存在特定 key
     *
     * @param context 上下文 {@link PluginContext}
     * @return 如果上下文中存在 key 则返回 true 否则返回 false
     */
    @Override
    public boolean support(PluginContext context) {
        return context.contains(getDependedKey());
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
    public abstract Object getDependedKey();

    /**
     * 解析后插件的 key
     *
     * @return 解析后插件的 key
     */
    public abstract Object getResolvedKey();
}
