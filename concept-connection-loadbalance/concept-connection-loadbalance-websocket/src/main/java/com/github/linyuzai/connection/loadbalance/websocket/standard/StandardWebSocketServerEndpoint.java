package com.github.linyuzai.connection.loadbalance.websocket.standard;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint("/concept-ws/{type}/{token}")
public class StandardWebSocketServerEndpoint implements WebSocketEndpoint {

    @OnOpen
    public void onOpen(Session session,
                       EndpointConfig config,
                       @PathParam(value = "type") String type,
                       @PathParam(value = "token") String token) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("type", type);
        metadata.put("token", token);
        add(session, metadata);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
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
