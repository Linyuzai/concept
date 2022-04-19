package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connections;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProxyConnectionSelector implements ConnectionSelector {

    @Override
    public ConnectionSelector broadcast(boolean broadcast) {
        return this;
    }

    @Override
    public boolean support(Message message) {
        return message.isProxy();
    }

    @Override
    public Connection select(Message message, Collection<Connection> connections) {
        List<Connection> list = connections.stream()
                .filter(Connection::isProxy)
                .collect(Collectors.toList());
        return Connections.of(list);
    }
}
