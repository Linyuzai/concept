package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connections;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
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
    public Connection doSelect(Message message, Collection<Connection> connections) {
        String header = message.getHeaders().get(name);
        List<Connection> list = connections.stream()
                .filter(it -> match(it, header))
                .collect(Collectors.toList());
        return Connections.of(list);
    }

    public abstract boolean match(Connection connection, String header);
}
