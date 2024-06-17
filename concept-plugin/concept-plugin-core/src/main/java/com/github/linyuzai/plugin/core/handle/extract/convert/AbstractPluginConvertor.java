package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

/**
 * {@link PluginConvertor} 抽象类。
 *
 * @param <T> 转换前的数据类型
 * @param <R> 转换后的数据类型
 */
public abstract class AbstractPluginConvertor<T, R> implements PluginConvertor {

    /**
     * 转换并发布 {@link PluginConvertedEvent} 事件
     *
     * @param source  转换前对象
     * @param context 上下文 {@link PluginContext}
     * @return 转换后对象
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Object source, PluginContext context) {
        PluginTree.Node inbound = (PluginTree.Node) source;
        PluginTree tree = context.get(PluginTree.class);
        PluginTree.Node outbound = tree.getTransformer()
                .create(this)
                .inbound(inbound)
                .transform(node -> node.map(it -> doConvert((T) it.getValue())))
                .outbound();
        context.publish(new PluginConvertedEvent(context, this, inbound, outbound));
        return outbound;
    }

    /**
     * 基于泛型转换
     *
     * @param source 转换前对象
     * @return 转换后对象
     */
    public abstract R doConvert(T source);
}
