package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

public interface ConnectionProxy {
    String FLAG = "concept-connection-proxy";
    Connection proxy(ConnectionServer server, ConnectionLoadBalanceConcept concept);
}
