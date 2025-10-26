package com.github.linyuzai.plugin.core.handle.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;

import java.util.Arrays;
import java.util.Collection;

/**
 * 名称过滤器
 */
@HandlerDependency(EntryResolver.class)
public class EntryFilter extends AntPathPluginFilter<Plugin.Entry> {

    public EntryFilter(String... patterns) {
        super(Arrays.asList(patterns));
    }

    public EntryFilter(Collection<String> patterns) {
        super(patterns);
    }

    /**
     * 匹配插件项
     */
    @Override
    public Object getKey() {
        return Plugin.Entry.class;
    }

    /**
     * 用插件项名称进行匹配
     */
    @Override
    protected String getMatchable(Plugin.Entry entry) {
        return entry.getName();
    }
}
