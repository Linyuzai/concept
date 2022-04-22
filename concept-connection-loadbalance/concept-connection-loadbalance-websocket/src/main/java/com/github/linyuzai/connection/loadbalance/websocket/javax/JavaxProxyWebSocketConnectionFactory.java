package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonProxyConnectionMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonProxyConnectionMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ProxyConnectionFactory;
import lombok.NonNull;

import javax.websocket.Session;
import java.util.Map;

public class JavaxProxyWebSocketConnectionFactory extends ProxyConnectionFactory {

    public JavaxProxyWebSocketConnectionFactory() {
        this(new JacksonProxyConnectionMessageEncoder(), new JacksonProxyConnectionMessageDecoder());
    }

    public JavaxProxyWebSocketConnectionFactory(@NonNull MessageEncoder messageEncoder, @NonNull MessageDecoder messageDecoder) {
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
