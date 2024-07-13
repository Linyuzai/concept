package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

/**
 * 插件转换器抽象类
 *
 * @param <T> 转换前的数据类型
 * @param <R> 转换后的数据类型
 */
public abstract class AbstractPluginConvertor<T, R> implements PluginConvertor {

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
        context.getConcept().getEventPublisher().publish(new PluginConvertedEvent(context, this, inbound, outbound));
        return outbound;
    }

    /**
     * 转换
     */
    public abstract R doConvert(T source);
}
