package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.PredicateModifierFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassSupplier;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * 类过滤器
 */
@HandlerDependency(ClassResolver.class)
public class ClassFilter extends PredicateModifierFilter<ClassSupplier, Class<?>> {

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
    protected Class<?> to(ClassSupplier classSupplier) {
        return classSupplier.get();
    }

    @Override
    public Object getKey() {
        return ClassSupplier.class;
    }
}
