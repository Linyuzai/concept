package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.TextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionFactory;
import lombok.NonNull;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Objects;

public class ServletWebSocketConnectionFactory extends WebSocketConnectionFactory<ServletWebSocketConnection> {

    @Override
    public boolean support(Object o, Map<Object, Object> metadata) {
        return o instanceof WebSocketSession;
    }

    @Override
    public ServletWebSocketConnection doCreate(Object o, Map<Object, Object> metadata) {
        WebSocketSession session = (WebSocketSession) o;
        return new ServletWebSocketConnection(session, Connection.Type.CLIENT, metadata);
    }
}
