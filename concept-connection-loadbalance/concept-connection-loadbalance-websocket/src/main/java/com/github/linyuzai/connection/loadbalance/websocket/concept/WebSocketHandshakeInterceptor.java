package com.github.linyuzai.connection.loadbalance.websocket.concept;

import java.util.Map;

public interface WebSocketHandshakeInterceptor {

    boolean onHandshake(WebSocketRequest request, WebSocketResponse response, Map<String, Object> attributes);
}
