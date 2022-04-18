package com.github.linyuzai.connection.loadbalance.websocket.standard;

import javax.websocket.*;

@ClientEndpoint
public class StandardWebSocketClientEndpoint implements WebSocketEndpoint {

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
