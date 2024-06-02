package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;

/**
 * {@link PluginFilter} 的抽象类
 *
 * @param <T> 插件类型
 */
@Getter
public abstract class AbstractPluginFilter<T> implements PluginFilter {

    protected boolean negate;

    /**
     * 取反
     *
     * @return {@link PluginFilter} 本身
     */
    @Override
    public PluginFilter negate() {
        this.negate = !this.negate;
        return this;
    }

    /**
     * 过滤。
     * 通过 key 获得插件数据，
     * 过滤之后重新设置，
     * 发布 {@link PluginFilteredEvent} 事件。
     *
     * @param context 上下文 {@link PluginContext}
     */
    @Override
    public void filter(PluginContext context) {
        Object inboundKey = getInboundKey();
        Object outboundKey = getOutboundKey();
        PluginTree tree = context.get(PluginTree.class);
        tree.getTransformer()
                .create(this)
                .inboundKey(inboundKey)
                .transform(node -> node.<T>filter(value -> applyNegation(doFilter(value))))
                .outboundKey(outboundKey);
        /*Object key = getKey();
        T original = context.get(key);
        if (original == null) {
            throw new PluginException("No plugin can be filtered with key: " + key);
        }
        T filtered = doFilter(original);
        context.set(key, filtered);
        context.publish(new PluginFilteredEvent(context, this, original, filtered));*/
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(PluginTree.class);
    }

    /**
     * 计算过滤与取反的最终值
     *
     * @param filter 是否过滤
     * @return 结合取反配置是否过滤
     */
    public boolean applyNegation(boolean filter) {
        return negate != filter;
    }

    public Object getInboundKey() {
        return getKey();
    }

    public Object getOutboundKey() {
        return getKey();
    }

    /**
     * 获得插件 key
     *
     * @return 插件的 key
     */
    public abstract Object getKey();

    /**
     * 以泛型的方式过滤
     *
     * @param original 需要过滤的插件
     * @return 过滤之后的插件
     */
    public abstract boolean doFilter(T original);

}
