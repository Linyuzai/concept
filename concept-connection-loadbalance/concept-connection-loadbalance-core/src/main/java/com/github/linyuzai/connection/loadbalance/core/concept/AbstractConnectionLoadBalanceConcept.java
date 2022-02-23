package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    private final Collection<Connection> connections = new CopyOnWriteArrayList<>();

    private ConnectionDiscoverer discoverer;

    private ConnectionSelector selector;

    @Override
    public void initialize() {
        Connection connection = discoverer.discover();
        if (connection != null) {
            connections.add(connection);
        }
    }

    @Override
    public void send(Object message) {
        send(message, selector);
    }

    @Override
    public void send(Object message, ConnectionSelector selector) {
        Connection connection = selector.select(connections);
        connection.send(message);
    }
}
