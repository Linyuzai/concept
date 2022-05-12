package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public interface ConnectionLoadBalanceConcept {

    void initialize();

    void destroy();

    void subscribe(boolean sendServerMsg);

    void subscribe(ConnectionServer server, boolean sendServerMsg);

    Connection create(Object o, Map<Object, Object> metadata);

    Connection onOpen(Object o, Map<Object, Object> metadata);

    void onOpen(Connection connection);

    void onClose(Object id, String type, Object reason);

    void onClose(Connection connection, Object reason);

    void onMessage(Object id, String type, Object message);

    void onMessage(Connection connection, Object message);

    void onError(Object id, String type, Throwable e);

    void onError(Connection connection, Throwable e);

    void send(Object msg);

    void send(Object msg, Map<String, String> headers);

    void publish(Object event);

    void move(Object id, String fromType, String toType, Consumer<Connection> consumer);

    void redefineType(Connection connection, String type, Connection.Redefiner redefiner);

    Connection getConnection(Object id, String type);

    Collection<Connection> getConnections(String type);
}
