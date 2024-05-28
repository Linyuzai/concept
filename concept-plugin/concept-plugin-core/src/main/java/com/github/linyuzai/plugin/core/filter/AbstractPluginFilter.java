package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link PluginFilter} 的抽象类
 *
 * @param <T> 插件类型
 */
public abstract class AbstractPluginFilter<T> implements PluginFilter {

    @Getter
    protected boolean negate;

    @Setter
    protected Class<? extends PluginResolver> filterWith;

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
        Object key = getKey();
        T original = context.get(key);
        if (original == null) {
            throw new PluginException("No plugin can be filtered with key: " + key);
        }
        T filtered = doFilter(original);
        context.set(key, filtered);
        context.publish(new PluginFilteredEvent(context, this, original, filtered));
    }

    @Override
    public boolean support(PluginContext context) {
        return true;
    }

    /**
     * 计算过滤与取反的最终值
     *
     * @param filter 是否过滤
     * @return 结合取反配置是否过滤
     */
    public boolean filterWithNegation(boolean filter) {
        return negate != filter;
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
    public abstract T doFilter(T original);

}
