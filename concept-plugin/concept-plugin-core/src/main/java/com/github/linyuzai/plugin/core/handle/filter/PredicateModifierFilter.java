package com.github.linyuzai.plugin.core.handle.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public abstract class PredicateModifierFilter<T> extends AbstractPluginFilter<T> {

    private final Collection<Predicate<T>> predicates;

    @SafeVarargs
    public PredicateModifierFilter(Predicate<T>... functions) {
        this(Arrays.asList(functions));
    }

    @Override
    public boolean doFilter(T original) {
        for (Predicate<T> predicate : predicates) {
            if (predicate.test(original)) {
                return true;
            }
        }
        return false;
    }
}
