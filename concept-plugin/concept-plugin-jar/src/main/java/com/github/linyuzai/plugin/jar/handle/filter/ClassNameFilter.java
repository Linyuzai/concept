package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.AntPathPluginFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassSupplier;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassResolver;

import java.util.Arrays;
import java.util.Collection;

/**
 * 类名过滤器
 */
@HandlerDependency(ClassResolver.class)
public class ClassNameFilter extends AntPathPluginFilter<ClassSupplier> {

    public ClassNameFilter(String... patterns) {
        this(Arrays.asList(patterns));
    }

    public ClassNameFilter(Collection<String> patterns) {
        super(patterns);
    }

    @Override
    protected String getMatchable(ClassSupplier classSupplier) {
        return classSupplier.getName();
    }

    @Override
    public Object getKey() {
        return ClassSupplier.class;
    }
}
