package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.StringMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.NonNull;

import javax.websocket.Session;
import java.util.Map;

public class JavaxWebSocketConnectionFactory extends AbstractConnectionFactory {

    public JavaxWebSocketConnectionFactory() {
        this(new JacksonMessageEncoder(), new StringMessageDecoder());
    }

    public JavaxWebSocketConnectionFactory(@NonNull MessageEncoder messageEncoder, @NonNull MessageDecoder messageDecoder) {
        super(messageEncoder, messageDecoder);
    }

    @Override
    public boolean support(Object o) {
        return o instanceof Session;
    }

    @Override
    public Connection create(Object o, Map<Object, Object> metadata) {
        JavaxWebSocketConnection connection = new JavaxWebSocketConnection((Session) o, metadata);
        connection.setMessageEncoder(messageEncoder);
        connection.setMessageDecoder(messageDecoder);
        return connection;
    }
}
