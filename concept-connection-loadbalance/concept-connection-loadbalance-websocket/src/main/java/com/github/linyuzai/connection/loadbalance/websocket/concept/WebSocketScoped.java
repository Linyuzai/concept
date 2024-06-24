package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

import java.util.Collection;
import java.util.List;

/**
 * WebSocket 作用域。
 * <p>
 * WebSocket scope.
 */
public interface WebSocketScoped extends Scoped {

    String NAME = WebSocketScoped.class.getSimpleName();

    @Override
    default boolean support(String scope) {
        return NAME.equals(scope);
    }

    static <S extends Scoped> List<S> filter(Collection<S> collection) {
        return Scoped.filter(NAME, collection);
    }
}
