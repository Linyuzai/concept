package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 基于消息头的选择器
 */
public abstract class MessageHeaderSelector extends AbstractConnectionSelector {

    @Override
    public boolean support(Message message) {
        return message.getHeaders().containsKey(getHeaderName());
    }

    @Override
    public Collection<Connection> doSelect(Message message, Collection<Connection> connections) {
        String header = message.getHeaders().get(getHeaderName());
        return connections.stream()
                .filter(it -> match(it, header))
                .collect(Collectors.toList());
    }

    public abstract String getHeaderName();

    public abstract boolean match(Connection connection, String header);
}
