package com.github.linyuzai.plugin.core.handle.extract.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.ContentResolver;

import java.lang.annotation.Annotation;

/**
 * 内容匹配器
 */
@HandlerDependency(ContentResolver.class)
public class ContentMatcher extends AbstractPluginMatcher<Plugin.Content> {

    public ContentMatcher(Annotation[] annotations) {
        super(annotations);
    }

    /**
     * 匹配内容对象
     */
    @Override
    public Object getKey() {
        return Plugin.Content.class;
    }

    @Override
    public boolean doFilter(Plugin.Content content, PluginContext context) {
        return true;
    }
}
