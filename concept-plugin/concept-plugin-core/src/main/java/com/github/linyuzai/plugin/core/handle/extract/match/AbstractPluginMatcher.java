package com.github.linyuzai.plugin.core.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.filter.EntryFilter;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;

import java.lang.annotation.Annotation;

/**
 * 插件匹配器抽象类
 */
@Getter
public abstract class AbstractPluginMatcher<T> implements PluginMatcher {

    /**
     * 条目过滤器
     */
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
            PluginMatchedEvent event = new PluginMatchedEvent(context, this, inboundKey, outbound);
            context.getConcept().getEventPublisher().publish(event);
            return outbound;
        }
        return null;
    }

    /**
     * 根据路径和名称进行过滤
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
     */
    public boolean hasContent(PluginTree.Node node) {
        return node.getSize(it -> !it.isPluginNode()) > 0;
    }

    /**
     * 用于提取上下文中的插件
     */
    public abstract Object getKey();

    /**
     * 过滤
     */
    public abstract boolean doFilter(T source, PluginContext context);
}
