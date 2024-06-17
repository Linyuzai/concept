package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerProperty;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.PatternPluginFilter;
import com.github.linyuzai.plugin.core.handle.filter.PluginFilter;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassNameResolver;

import java.util.Arrays;
import java.util.Collection;

/**
 * 类名过滤器
 */
@HandlerProperty(PluginFilter.PREFIX + "classname")
@HandlerDependency(JarClassNameResolver.class)
public class ClassNameFilter extends PatternPluginFilter<String> {

    public ClassNameFilter(String... patterns) {
        this(Arrays.asList(patterns));
    }

    public ClassNameFilter(Collection<String> patterns) {
        super(patterns);
    }

    @Override
    protected String getMatchable(String className) {
        return className;
    }

    @Override
    public Object getKey() {
        return JarPlugin.ClassName.class;
    }
}
