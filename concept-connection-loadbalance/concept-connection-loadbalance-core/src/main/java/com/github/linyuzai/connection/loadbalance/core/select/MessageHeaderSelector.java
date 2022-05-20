package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public abstract class MessageHeaderSelector extends AbstractConnectionSelector {

    private String name;

    @Override
    public boolean support(Message message) {
        return message.getHeaders().containsKey(name);
    }

    @Override
    public Collection<Connection> doSelect(Message message, Collection<Connection> connections) {
        String header = message.getHeaders().get(name);
        return connections.stream()
                .filter(it -> match(it, header))
                .collect(Collectors.toList());
    }

    public abstract boolean match(Connection connection, String header);
}
