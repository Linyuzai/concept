package com.github.linyuzai.plugin.core.handle.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;

/**
 * 插件过滤器抽象类
 *
 * @param <T> 插件类型
 */
@Getter
public abstract class AbstractPluginFilter<T> implements PluginFilter {

    /**
     * 是否取反
     */
    protected boolean negate;

    /**
     * 取反
     */
    @Override
    public AbstractPluginFilter<T> negate() {
        this.negate = !this.negate;
        return this;
    }

    /**
     * 过滤，
     * 通过 key 获得插件数据，
     * 过滤之后重新设置
     */
    @SuppressWarnings("unchecked")
    @Override
    public void filter(PluginContext context) {
        Object inboundKey = getInboundKey();
        Object outboundKey = getOutboundKey();
        PluginTree tree = context.get(PluginTree.class);
        tree.getTransformer()
                .create(this)
                .inboundKey(inboundKey)
                .transform(node -> node.filter(it -> applyNegation(doFilter((T) it.getValue()))))
                .outboundKey(outboundKey);
        context.getConcept().getEventPublisher().publish(new PluginFilteredEvent(context, this, inboundKey, outboundKey));
    }

    /**
     * 计算过滤与取反的最终值
     */
    public boolean applyNegation(boolean filter) {
        return negate != filter;
    }

    /**
     * 去要过滤的数据对应的 key
     */
    public Object getInboundKey() {
        return getKey();
    }

    /**
     * 数据过滤后输出的 key
     */
    public Object getOutboundKey() {
        return getKey();
    }

    /**
     * 获得插件 key
     */
    public abstract Object getKey();

    /**
     * 过滤
     */
    public abstract boolean doFilter(T original);

}
