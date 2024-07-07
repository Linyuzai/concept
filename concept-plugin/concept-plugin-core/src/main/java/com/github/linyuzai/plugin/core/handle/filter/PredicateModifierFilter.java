package com.github.linyuzai.plugin.core.handle.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;


@Getter
@RequiredArgsConstructor
public abstract class PredicateModifierFilter<T, P> extends AbstractPluginFilter<T> {

    private final Collection<Predicate<P>> predicates;

    @SafeVarargs
    public PredicateModifierFilter(Predicate<P>... functions) {
        this(Arrays.asList(functions));
    }

    @Override
    public boolean doFilter(T original) {
        P p = to(original);
        for (Predicate<P> predicate : predicates) {
            if (predicate.test(p)) {
                return true;
            }
        }
        return false;
    }

    protected abstract P to(T t);
}
