package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.TextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.NonNull;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class ServletWebSocketConnectionFactory extends AbstractConnectionFactory {

    public ServletWebSocketConnectionFactory() {
        this(new JacksonMessageEncoder(), new TextMessageDecoder());
    }

    public ServletWebSocketConnectionFactory(@NonNull MessageEncoder messageEncoder, @NonNull MessageDecoder messageDecoder) {
        super(messageEncoder, messageDecoder);
    }

    @Override
    public boolean support(Object o, Map<Object, Object> metadata) {
        return o instanceof WebSocketSession;
    }

    @Override
    public Connection create(Object o, Map<Object, Object> metadata) {
        ServletWebSocketConnection connection =
                new ServletWebSocketConnection((WebSocketSession) o, Connection.Type.CLIENT, metadata);
        connection.setMessageEncoder(messageEncoder);
        connection.setMessageDecoder(messageDecoder);
        return connection;
    }
}
