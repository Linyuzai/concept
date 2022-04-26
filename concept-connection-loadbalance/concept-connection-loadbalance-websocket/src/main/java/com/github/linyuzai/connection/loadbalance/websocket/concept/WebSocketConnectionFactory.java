package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;

import java.util.Map;

public abstract class WebSocketConnectionFactory<T extends WebSocketConnection>
        extends AbstractConnectionFactory<T, WebSocketLoadBalanceConcept> {

    @Override
    public T doCreate(Object o, Map<Object, Object> metadata, WebSocketLoadBalanceConcept concept) {
        T connection = doCreate(o, metadata);
        MessageCodecAdapter adapter = concept.getMessageCodecAdapter();
        connection.setMessageEncoder(adapter.getMessageEncoder(Connection.Type.CLIENT));
        connection.setMessageDecoder(adapter.getMessageDecoder(Connection.Type.CLIENT));
        connection.setConcept(concept);
        return connection;
    }

    public abstract T doCreate(Object o, Map<Object, Object> metadata);
}
