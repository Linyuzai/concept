package com.github.linyuzai.connection.loadbalance.core.discovery;

import com.github.linyuzai.connection.loadbalance.core.concept.Connections;

import java.util.Collection;

public class DiscoveryConnections extends Connections implements DiscoveryConnection {

    public DiscoveryConnections(Collection<? extends DiscoveryConnection> connections) {
        super(connections);
    }
}
