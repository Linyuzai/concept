package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

import java.util.function.Consumer;

public interface ConnectionSubscriber {
    void subscribe(ConnectionServer server, ConnectionLoadBalanceConcept concept, Consumer<Connection> consumer);
}
