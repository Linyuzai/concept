package com.github.linyuzai.connection.loadbalance.core.scope;

import java.util.Collection;

/**
 * 支持定义连接域的工厂。
 * <p>
 * Factory which supports set scope.
 */
public interface ScopedFactory<T> extends Scoped {

    T create(String scope);

    static <C, F extends ScopedFactory<C>> C create(String scope, Class<C> type, Collection<F> factories) {
        for (F factory : factories) {
            if (factory.support(scope)) {
                return factory.create(scope);
            }
        }
        throw new IllegalArgumentException("No " + type.getName() + " supported");
    }
}
