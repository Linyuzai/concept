package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.PredicateModifierFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * {@link java.lang.reflect.Modifier} 过滤器
 */
@Getter
@RequiredArgsConstructor
@HandlerDependency(JarClassResolver.class)
public class ClassModifierFilter extends PredicateModifierFilter<JarClass, Integer> {

    @SafeVarargs
    public ClassModifierFilter(Predicate<Integer>... predicates) {
        super(predicates);
    }

    public ClassModifierFilter(Collection<Predicate<Integer>> predicates) {
        super(predicates);
    }

    @Override
    protected Integer to(JarClass jarClass) {
        return jarClass.get().getModifiers();
    }

    @Override
    public Object getKey() {
        return JarClass.class;
    }
}
