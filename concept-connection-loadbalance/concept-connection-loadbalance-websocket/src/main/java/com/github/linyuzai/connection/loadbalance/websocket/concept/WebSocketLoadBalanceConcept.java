package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactoryAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelectorAdapter;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;

public class WebSocketLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    private static WebSocketLoadBalanceConcept instance;

    public WebSocketLoadBalanceConcept(ConnectionServerProvider connectionServerProvider, ConnectionProxy connectionProxy, ConnectionSelectorAdapter connectionSelectorAdapter, MessageFactoryAdapter messageFactoryAdapter, MessageEncoder messageEncoder, MessageDecoder messageDecoder) {
        super(connectionServerProvider, connectionProxy, connectionSelectorAdapter, messageFactoryAdapter, messageEncoder, messageDecoder);
        instance = this;
    }

    public static WebSocketLoadBalanceConcept getInstance() {
        return instance;
    }
}
