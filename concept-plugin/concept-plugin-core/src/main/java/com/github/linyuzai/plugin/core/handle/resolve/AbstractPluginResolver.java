package com.github.linyuzai.plugin.core.handle.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

/**
 * 插件解析器抽象类
 *
 * @param <T> 输入类型
 * @param <R> 输出类型
 */
public abstract class AbstractPluginResolver<T, R> implements PluginResolver {

    /**
     * 转换插件树
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
        PluginResolvedEvent event = new PluginResolvedEvent(context, this, inboundKey, outboundKey);
        context.getConcept().getEventPublisher().publish(event);
    }

    /**
     * 过滤
     */
    public boolean doFilter(T source, PluginContext context) {
        return true;
    }

    /**
     * 解析
     */
    public abstract R doResolve(T source, PluginContext context);

    /**
     * 输入数据的key
     */
    public abstract Object getInboundKey();

    /**
     * 输出数据key
     */
    public abstract Object getOutboundKey();
}
