package com.github.linyuzai.connection.loadbalance.websocket.standard;

import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint(ConnectionProxy.ENDPOINT_PREFIX + "{type}")
public class StandardWebSocketProxyEndpoint implements WebSocketEndpoint {

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "type") String type) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ConnectionProxy.HEADER, type);
        add(session, metadata);
    }

    @OnClose
    public void onClose(Session session) {
        remove(session);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        message(session, message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        error(session, e);
    }
}
