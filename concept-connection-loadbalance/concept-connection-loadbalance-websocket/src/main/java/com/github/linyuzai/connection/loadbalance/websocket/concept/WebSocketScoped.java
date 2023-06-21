package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

public interface WebSocketScoped extends Scoped {

    String NAME = WebSocketScoped.class.getSimpleName();

    @Override
    default boolean support(String scope) {
        return NAME.equals(scope);
    }
}
