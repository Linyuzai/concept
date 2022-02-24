package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.discovery.DiscoveryConnection;

import java.util.ArrayList;
import java.util.Collection;

public interface ConnectionFilter extends ConnectionSelector {

    boolean filter(Connection connection);

    @Override
    default Connection select(Collection<Connection> connections, DiscoveryConnection discoveryConnection) {
        Collection<Connection> filteredConnections = new ArrayList<>();
        for (Connection c : connections) {
            if (filter(c)) {
                filteredConnections.add(c);
            }
        }
        filteredConnections.add(discoveryConnection);
        return new Connections(filteredConnections);
    }
}
