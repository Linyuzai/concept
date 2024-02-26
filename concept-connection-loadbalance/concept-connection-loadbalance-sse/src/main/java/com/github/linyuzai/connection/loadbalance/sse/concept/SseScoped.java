package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * SSE 作用域。
 * <p>
 * SSE scope.
 */
public interface SseScoped extends Scoped {

    String NAME = SseScoped.class.getSimpleName();

    @Override
    default boolean support(String scope) {
        return NAME.equals(scope);
    }
}
