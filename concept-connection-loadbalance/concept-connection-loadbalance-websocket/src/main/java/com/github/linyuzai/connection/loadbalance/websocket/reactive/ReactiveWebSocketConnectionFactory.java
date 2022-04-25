package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.TextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.util.Map;

public class ReactiveWebSocketConnectionFactory extends AbstractConnectionFactory {

    public ReactiveWebSocketConnectionFactory() {
        this(new JacksonMessageEncoder(), new TextMessageDecoder());
    }

    public ReactiveWebSocketConnectionFactory(@NonNull MessageEncoder messageEncoder, @NonNull MessageDecoder messageDecoder) {
        super(messageEncoder, messageDecoder);
    }

    @Override
    public boolean support(Object o, Map<Object, Object> metadata) {
        return o instanceof Object[] &&
                ((Object[]) o).length == 2 &&
                ((Object[]) o)[0] instanceof WebSocketSession &&
                ((Object[]) o)[1] instanceof FluxSink;
    }

    @Override
    public Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        WebSocketSession session = (WebSocketSession) ((Object[]) o)[0];
        @SuppressWarnings("unchecked")
        FluxSink<WebSocketMessage> sender = (FluxSink<WebSocketMessage>) ((Object[]) o)[1];
        ReactiveWebSocketConnection connection =
                new ReactiveWebSocketConnection(session, sender, Connection.Type.CLIENT, metadata);
        if (!connection.getMetadata().containsKey(Connection.URI)) {
            connection.getMetadata().put(Connection.URI, session.getHandshakeInfo().getUri().toString());
        }
        connection.setMessageEncoder(messageEncoder);
        connection.setMessageDecoder(messageDecoder);
        connection.setConcept(concept);
        return connection;
    }
}
