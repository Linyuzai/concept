package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.TextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.NonNull;

import javax.websocket.Session;
import java.util.Map;

public class JavaxWebSocketConnectionFactory extends AbstractConnectionFactory {

    public JavaxWebSocketConnectionFactory() {
        this(new JacksonMessageEncoder(), new TextMessageDecoder());
    }

    public JavaxWebSocketConnectionFactory(@NonNull MessageEncoder messageEncoder, @NonNull MessageDecoder messageDecoder) {
        super(messageEncoder, messageDecoder);
    }

    @Override
    public boolean support(Object o, Map<Object, Object> metadata) {
        return o instanceof Session;
    }

    @Override
    public Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        Session session = (Session) o;
        JavaxWebSocketConnection connection =
                new JavaxWebSocketConnection(session, Connection.Type.CLIENT, metadata);
        if (!connection.getMetadata().containsKey(Connection.URI)) {
            connection.getMetadata().put(Connection.URI, session.getRequestURI().toString());
        }
        connection.setMessageEncoder(messageEncoder);
        connection.setMessageDecoder(messageDecoder);
        connection.setConcept(concept);
        return connection;
    }
}
