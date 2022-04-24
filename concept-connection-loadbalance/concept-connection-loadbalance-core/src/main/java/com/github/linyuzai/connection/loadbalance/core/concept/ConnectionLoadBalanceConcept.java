package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Map;

public interface ConnectionLoadBalanceConcept {

    void initialize();

    void destroy();

    Connection create(Object o, Map<Object, Object> metadata);

    Connection open(Object o, Map<Object, Object> metadata);

    void open(Connection connection);

    void close(Object id, String type, Object reason);

    void close(Connection connection, Object reason);

    void message(Object id, String type, byte[] message);

    void message(Connection connection, byte[] message);

    void error(Object id, String type, Throwable e);

    void error(Connection connection, Throwable e);

    void send(Object msg);

    void send(Object msg, Map<String, String> headers);

    void publish(Object event);

    Connection getConnection(Object id, String type);

    Map<Object, Connection> getConnections(String type);
}
