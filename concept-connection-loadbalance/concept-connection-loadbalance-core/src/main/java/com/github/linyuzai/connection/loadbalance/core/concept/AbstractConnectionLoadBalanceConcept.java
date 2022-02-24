package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.discovery.ConnectionDiscoverer;
import com.github.linyuzai.connection.loadbalance.core.discovery.DiscoveryConnection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactoryAdapter;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    private final Collection<Connection> connections = new CopyOnWriteArrayList<>();

    private DiscoveryConnection discoveryConnection;

    private ConnectionDiscoverer connectionDiscoverer;

    private ConnectionSelector connectionSelector;
    
    private MessageFactoryAdapter messageFactoryAdapter;

    @Override
    public void initialize() {
        discoveryConnection = connectionDiscoverer.discover();
    }

    @Override
    public void send(Object message) {
        send(message, connectionSelector);
    }

    @Override
    public void send(Object message, ConnectionSelector selector) {
        Connection connection = selector.select(connections, discoveryConnection);
        MessageFactory messageFactory = messageFactoryAdapter.getMessageFactory(message);
        Message messageCreated = messageFactory.create(message);
        connection.send(messageCreated);
    }
}
