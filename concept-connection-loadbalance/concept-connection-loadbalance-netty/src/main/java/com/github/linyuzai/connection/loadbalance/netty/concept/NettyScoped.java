package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

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
}
