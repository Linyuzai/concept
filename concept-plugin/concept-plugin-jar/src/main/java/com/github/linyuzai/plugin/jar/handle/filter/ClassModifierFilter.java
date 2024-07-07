package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.PredicateModifierFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassSupplier;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * {@link java.lang.reflect.Modifier} 过滤器
 */
@Getter
@RequiredArgsConstructor
@HandlerDependency(ClassResolver.class)
public class ClassModifierFilter extends PredicateModifierFilter<ClassSupplier, Integer> {

    @SafeVarargs
    public ClassModifierFilter(Predicate<Integer>... predicates) {
        super(predicates);
    }

    public ClassModifierFilter(Collection<Predicate<Integer>> predicates) {
        super(predicates);
    }

    @Override
    protected Integer to(ClassSupplier classSupplier) {
        return classSupplier.get().getModifiers();
    }

    @Override
    public Object getKey() {
        return ClassSupplier.class;
    }
}
