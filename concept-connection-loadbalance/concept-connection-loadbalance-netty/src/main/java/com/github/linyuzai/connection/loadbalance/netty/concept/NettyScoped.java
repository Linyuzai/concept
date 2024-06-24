package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

import java.util.Collection;
import java.util.List;

/**
 * Netty 作用域。
 * <p>
 * Netty scope.
 */
public interface NettyScoped extends Scoped {

    String NAME = NettyScoped.class.getSimpleName();

    @Override
    default boolean support(String scope) {
        return NAME.equals(scope);
    }

    static <S extends Scoped> List<S> filter(Collection<S> collection) {
        return Scoped.filter(NAME, collection);
    }
}
