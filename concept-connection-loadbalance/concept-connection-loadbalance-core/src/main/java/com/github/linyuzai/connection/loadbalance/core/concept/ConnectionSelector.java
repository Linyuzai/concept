package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.discovery.DiscoveryConnection;

import java.util.Collection;

public interface ConnectionSelector {

    Connection select(Collection<Connection> connections, DiscoveryConnection discoveryConnection);
}
