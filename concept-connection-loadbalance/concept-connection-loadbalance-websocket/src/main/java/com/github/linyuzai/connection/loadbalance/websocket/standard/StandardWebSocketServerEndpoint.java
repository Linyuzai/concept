package com.github.linyuzai.connection.loadbalance.websocket.standard;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

@ServerEndpoint("/ws/{type}/{token}")
public class StandardWebSocketServerEndpoint {

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "type") String type, @PathParam(value = "token") String token) {
        ServerEndpointConfig config = ServerEndpointConfig.Builder.create(null,"/concept/websocket/{}").build();
    }

    @OnClose
    public void onClose(Session session, @PathParam(value = "type") String type) {

    }

    @OnMessage
    public void onMessage(Session session, @PathParam(value = "type") String type, String message) {

    }

    @OnError
    public void onError(Throwable e) {

    }
}
