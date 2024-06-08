package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.resolve.ContentResolver;

import java.lang.annotation.Annotation;

/**
 * 内容匹配器
 */
@HandlerDependency(ContentResolver.class)
public class ContentMatcher extends AbstractPluginMatcher<Plugin.Content> {

    public ContentMatcher(Annotation[] annotations) {
        super(annotations);
    }

    @Override
    public Object getKey() {
        return Plugin.Content.class;
    }

    @Override
    public boolean doFilter(Plugin.Content source, PluginContext context) {
        return true;
    }
}
