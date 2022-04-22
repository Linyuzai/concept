package com.github.linyuzai.connection.loadbalance.core.concept;

import java.util.Map;

public interface ConnectionLoadBalanceConcept {

    void initialize();

    void destroy();

    Connection create(Object o, Map<Object, Object> metadata);

    Connection open(Object o, Map<Object, Object> metadata, Connection.Type type);

    void open(Connection connection, Connection.Type type);

    void close(Object id, Connection.Type type);

    void message(Object id, byte[] message, Connection.Type type);

    void error(Object id, Throwable e, Connection.Type type);

    void send(Object msg);

    void send(Object msg, Map<String, String> headers);

    void publish(Object event);

    Connection getConnection(Object id, Connection.Type type);

    Map<Object, Connection> getConnections(Connection.Type type);
}
