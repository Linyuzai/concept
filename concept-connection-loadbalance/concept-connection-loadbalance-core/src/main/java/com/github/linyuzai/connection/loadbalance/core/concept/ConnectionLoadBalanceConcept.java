package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;

public interface ConnectionLoadBalanceConcept {

    void initialize();

    void destroy();

    void add(Connection connection);

    void remove(Object id);

    void message(Object id, byte[] message);

    void error(Object id, Throwable e);

    Connection get(Object id);

    void send(Object message);

    MessageEncoder getMessageEncoder();
}
