package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;

import java.util.List;

public class WebSocketLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    private static WebSocketLoadBalanceConcept instance;

    public WebSocketLoadBalanceConcept(ConnectionServerProvider connectionServerProvider, ConnectionProxy connectionProxy, MessageEncoder messageEncoder, MessageDecoder messageDecoder, List<MessageFactory> messageFactories, List<ConnectionFactory> connectionFactories, List<ConnectionSelector> connectionSelectors) {
        super(connectionServerProvider, connectionProxy, messageEncoder, messageDecoder, messageFactories, connectionFactories, connectionSelectors);
        instance = this;
    }


    public static WebSocketLoadBalanceConcept getInstance() {
        return instance;
    }

    public static class Builder extends AbstractBuilder<Builder> {

        public WebSocketLoadBalanceConcept build() {
            preBuild();
            return new WebSocketLoadBalanceConcept(
                    connectionServerProvider,
                    connectionProxy,
                    messageEncoder,
                    messageDecoder,
                    messageFactories,
                    connectionFactories,
                    connectionSelectors);
        }
    }
}
