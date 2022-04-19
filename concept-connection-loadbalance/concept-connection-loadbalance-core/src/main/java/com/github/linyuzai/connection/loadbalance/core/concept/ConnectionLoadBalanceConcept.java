package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;

import java.util.List;
import java.util.Map;

public interface ConnectionLoadBalanceConcept {

    void initialize();

    void destroy();

    void add(Connection connection);

    void remove(Object id);

    void message(Object id, byte[] message);

    void error(Object id, Throwable e);

    Connection get(Object id);

    void send(Object message);

    Map<Object, Connection> getConnections();

    ConnectionServerProvider getConnectionServerProvider();

    ConnectionProxy getConnectionProxy();

    MessageEncoder getMessageEncoder();

    MessageDecoder getMessageDecoder();

    List<MessageFactory> getMessageFactories();

    List<ConnectionSelector> getConnectionSelectors();
}
