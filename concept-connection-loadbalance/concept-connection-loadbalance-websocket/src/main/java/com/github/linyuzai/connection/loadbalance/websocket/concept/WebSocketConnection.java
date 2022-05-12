package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConceptAware;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;

import java.util.Map;

public abstract class WebSocketConnection extends AbstractConnection
        implements ConnectionLoadBalanceConceptAware<WebSocketLoadBalanceConcept> {

    public WebSocketConnection(String type) {
        super(type);
    }

    public WebSocketConnection(String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void setConnectionLoadBalanceConcept(WebSocketLoadBalanceConcept concept) {
        MessageCodecAdapter adapter = concept.getMessageCodecAdapter();
        setMessageEncoder(adapter.getMessageEncoder(getType()));
        setMessageDecoder(adapter.getMessageDecoder(getType()));
        setConcept(concept);
    }

    public abstract boolean isOpen();
}
