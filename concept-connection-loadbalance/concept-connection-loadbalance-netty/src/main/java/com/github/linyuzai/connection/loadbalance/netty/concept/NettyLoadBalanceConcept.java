package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;

import java.util.List;

public class NettyLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    public NettyLoadBalanceConcept(ConnectionServerProvider connectionServerProvider, ConnectionSubscriber connectionSubscriber, List<ConnectionFactory> connectionFactories, List<ConnectionSelector> connectionSelectors, List<MessageFactory> messageFactories, MessageCodecAdapter messageCodecAdapter, ConnectionEventPublisher eventPublisher) {
        super(connectionServerProvider, connectionSubscriber, connectionFactories, connectionSelectors, messageFactories, messageCodecAdapter, eventPublisher);
    }
}
