package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connections;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MetadataSelector extends AbstractConnectionSelector {

    private String name;

    @Override
    public boolean support(Message message) {
        return message.getHeaders().containsKey(name);
    }

    @Override
    public Connection doSelect(Message message, Collection<Connection> connections) {
        String s = message.getHeaders().get(name);
        List<Connection> list = connections.stream()
                .filter(it -> Objects.equals(it.getMetadata().get(name), s))
                .collect(Collectors.toList());
        return Connections.of(list);
    }
}
