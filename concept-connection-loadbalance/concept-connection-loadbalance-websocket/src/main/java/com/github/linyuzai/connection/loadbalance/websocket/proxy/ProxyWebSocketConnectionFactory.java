package com.github.linyuzai.connection.loadbalance.websocket.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.proxy.ProxyConnectionFactory;

import javax.websocket.Session;
import java.util.Map;

public class ProxyWebSocketConnectionFactory extends ProxyConnectionFactory {

    @Override
    public Connection create(Object o, Map<String, String> metadata) {
        return null;
    }

    @Override
    public boolean support(Object o) {
        return o instanceof Session;
    }
}
