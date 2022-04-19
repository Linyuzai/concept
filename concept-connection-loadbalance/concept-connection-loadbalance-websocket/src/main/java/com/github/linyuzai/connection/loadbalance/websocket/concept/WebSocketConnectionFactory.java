package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import javax.websocket.Session;
import java.util.Map;

public class WebSocketConnectionFactory extends AbstractConnectionFactory {

    @Override
    public boolean support(Object o) {
        return o instanceof Session;
    }

    @Override
    public Connection create(Object o, Map<String, String> metadata) {
        return new WebSocketConnection((Session) o, metadata);
    }
}
