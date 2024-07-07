package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.PredicateModifierFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * 类过滤器
 */
@HandlerDependency(JarClassResolver.class)
public class ClassFilter extends PredicateModifierFilter<JarClass, Class<?>> {

    public ClassFilter(Class<?>... classes) {
        this(Arrays.asList(classes));
    }

    public ClassFilter(Collection<Class<?>> classes) {
        super(cls -> {
            for (Class<?> c : classes) {
                if (c.isAssignableFrom(cls)) {
                    return true;
                }
            }
            return false;
        });
    }

    @SafeVarargs
    public ClassFilter(Predicate<Class<?>>... functions) {
        super(functions);
    }

    @Override
    protected Class<?> to(JarClass jarClass) {
        return jarClass.get();
    }

    @Override
    public Object getKey() {
        return JarClass.class;
    }
}
