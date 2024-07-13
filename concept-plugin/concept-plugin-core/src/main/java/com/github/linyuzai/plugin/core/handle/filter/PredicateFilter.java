package com.github.linyuzai.plugin.core.handle.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * 基于断言的过滤器
 */
@Getter
@RequiredArgsConstructor
public abstract class PredicateFilter<T> extends AbstractPluginFilter<T> {

    private final Collection<Predicate<T>> predicates;

    @SafeVarargs
    public PredicateFilter(Predicate<T>... functions) {
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
