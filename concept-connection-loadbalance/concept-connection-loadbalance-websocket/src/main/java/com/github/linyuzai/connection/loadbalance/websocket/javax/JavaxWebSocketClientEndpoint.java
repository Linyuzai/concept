package com.github.linyuzai.connection.loadbalance.websocket.javax;

import javax.websocket.*;

@ClientEndpoint
public class JavaxWebSocketClientEndpoint implements JavaxWebSocketEndpoint {

    @OnClose
    public void onClose(Session session) {
        JavaxWebSocketConnectionProxy.getConcept().remove(session.getId());
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        JavaxWebSocketConnectionProxy.getConcept().message(session.getId(), message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        JavaxWebSocketConnectionProxy.getConcept().error(session.getId(), e);
    }
}
