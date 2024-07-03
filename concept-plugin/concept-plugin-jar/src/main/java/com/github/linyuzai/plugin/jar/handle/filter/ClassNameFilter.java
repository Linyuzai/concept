package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.PatternPluginFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassResolver;

import java.util.Arrays;
import java.util.Collection;

/**
 * 类名过滤器
 */
@HandlerDependency(JarClassResolver.class)
public class ClassNameFilter extends PatternPluginFilter<JarClass> {

    public ClassNameFilter(String... patterns) {
        this(Arrays.asList(patterns));
    }

    public ClassNameFilter(Collection<String> patterns) {
        super(patterns);
    }

    @Override
    protected String getMatchable(JarClass jarClass) {
        return jarClass.getName();
    }

    @Override
    public Object getKey() {
        return JarClass.class;
    }
}
