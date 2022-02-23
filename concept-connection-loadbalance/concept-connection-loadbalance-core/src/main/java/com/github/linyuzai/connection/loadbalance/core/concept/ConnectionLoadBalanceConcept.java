package com.github.linyuzai.connection.loadbalance.core.concept;

public interface ConnectionLoadBalanceConcept {

    void initialize();

    void send(Object message);

    void send(Object message, ConnectionSelector selector);
}
