package com.github.linyuzai.plugin.core.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.filter.EntryFilter;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @param <T> 插件类型
 */
@Getter
public abstract class AbstractPluginMatcher<T> implements PluginMatcher {

    private EntryFilter entryFilter;

    public AbstractPluginMatcher(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == PluginEntry.class) {
                String[] patterns = ((PluginEntry) annotation).value();
                if (patterns.length > 0) {
                    entryFilter = new EntryFilter(patterns);
                }
            }
        }
    }

    /**
     * 匹配并发布 {@link PluginMatchedEvent} 事件
     *
     * @param context 上下文 {@link PluginContext}
     * @return 匹配到的插件对象
     */
    @Override
    public Object match(PluginContext context) {
        Object inboundKey = getKey();
        PluginTree tree = context.get(PluginTree.class);
        PluginTree.Node outbound = tree.getTransformer()
                .create(this)
                .inboundKey(inboundKey)
                .transform(node -> node.filter(it -> filter(it, context)))
                .outbound();
        if (hasContent(outbound)) {
            context.publish(new PluginMatchedEvent(context, this, inboundKey, outbound));
            return outbound;
        }
        return null;
    }

    /**
     * 结合路径和名称进行过滤
     *
     * @param node 路径名称
     * @return 是否满足过滤条件
     */
    @SuppressWarnings("unchecked")
    public boolean filter(PluginTree.Node node, PluginContext context) {
        if (entryFilter != null && !entryFilter.matchPattern(node.getName())) {
            return false;
        }
        return doFilter((T) node.getValue(), context);
    }

    /**
     * 过滤后的插件是否为空
     *
     * @param node 过滤后的插件
     * @return 如果为空返回 true 否则返回 false
     */
    public boolean hasContent(PluginTree.Node node) {
        AtomicInteger size = new AtomicInteger(0);
        node.forEach(it -> {
            if (!it.isPluginNode()) {
                size.incrementAndGet();
            }
        });
        return size.get() > 0;
    }

    /**
     * 用于提取上下文中的插件
     *
     * @return 插件在上下文中的 key
     */
    public abstract Object getKey();

    /**
     * 基于泛型的插件过滤匹配
     *
     * @param source 被过滤的插件
     * @return 过滤后的插件
     */
    public abstract boolean doFilter(T source, PluginContext context);
}
