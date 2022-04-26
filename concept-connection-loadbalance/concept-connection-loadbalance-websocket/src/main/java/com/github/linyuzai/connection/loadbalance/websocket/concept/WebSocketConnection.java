package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;

import java.util.Map;

public abstract class WebSocketConnection extends AbstractConnection {

    public WebSocketConnection(String type) {
        super(type);
    }

    public WebSocketConnection(String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }
}
