package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

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
    @SuppressWarnings("unchecked")
    @Override
    public void resolve(PluginContext context) {
        Object inboundKey = getInboundKey();
        Object outboundKey = getOutboundKey();
        PluginTree tree = context.get(PluginTree.class);
        tree.getTransformer()
                .create(this)
                .inboundKey(inboundKey)
                .transform(node -> node.map(
                        it -> doResolve((T) it.getValue(), context),
                        it -> doFilter((T) it.getValue(), context)))
                .outboundKey(outboundKey);
        context.publish(new PluginResolvedEvent(context, this, inboundKey, outboundKey));
    }

    public boolean doFilter(T source, PluginContext context) {
        return true;
    }

    /**
     * 基于泛型及上下文 {@link PluginContext} 的插件解析
     *
     * @param source  未解析的插件
     * @param context 上下文 {@link PluginContext}
     * @return 解析后的插件
     */
    public abstract R doResolve(T source, PluginContext context);

    /**
     * 未解析的插件的 key
     *
     * @return 未解析的插件的 key
     */
    public abstract Object getInboundKey();

    /**
     * 解析后插件的 key
     *
     * @return 解析后插件的 key
     */
    public abstract Object getOutboundKey();
}
