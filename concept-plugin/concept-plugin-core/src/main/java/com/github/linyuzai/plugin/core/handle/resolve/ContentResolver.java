package com.github.linyuzai.plugin.core.handle.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;

/**
 * 内容解析器
 */
@HandlerDependency(EntryResolver.class)
public class ContentResolver extends AbstractPluginResolver<Plugin.Entry, Plugin.Content> {

    /**
     * 过滤内容不为 null
     */
    @Override
    public boolean doFilter(Plugin.Entry source, PluginContext context) {
        return source.getContent() != null;
    }

    /**
     * 转换成内容对象
     */
    @Override
    public Plugin.Content doResolve(Plugin.Entry source, PluginContext context) {
        return source.getContent();
    }

    /**
     * 解析插件项
     */
    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

    /**
     * 解析成内容对象
     */
    @Override
    public Object getOutboundKey() {
        return Plugin.Content.class;
    }
}
