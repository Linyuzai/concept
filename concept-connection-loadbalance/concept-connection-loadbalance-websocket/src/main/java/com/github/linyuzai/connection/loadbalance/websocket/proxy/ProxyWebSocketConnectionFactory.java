package com.github.linyuzai.connection.loadbalance.websocket.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.JacksonProxyConnectionMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.JacksonProxyConnectionMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.ProxyConnectionFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import lombok.NonNull;

import javax.websocket.Session;
import java.util.Map;

public class ProxyWebSocketConnectionFactory extends ProxyConnectionFactory {

    public ProxyWebSocketConnectionFactory() {
        this(new JacksonProxyConnectionMessageEncoder(), new JacksonProxyConnectionMessageDecoder());
    }

    public ProxyWebSocketConnectionFactory(@NonNull MessageEncoder messageEncoder, @NonNull MessageDecoder messageDecoder) {
        super(messageEncoder, messageDecoder);
    }

    @Override
    public boolean support(Object o) {
        return o instanceof Session;
    }

    @Override
    public Connection create(Object o, Map<String, String> metadata) {
        WebSocketConnection connection = new WebSocketConnection((Session) o, metadata);
        connection.setMessageEncoder(messageEncoder);
        connection.setMessageDecoder(messageDecoder);
        return connection;
    }
}
