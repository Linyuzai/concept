package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class FilterConnectionSelectorChain implements ConnectionSelector {

    private final List<FilterConnectionSelector> selectors;

    @Override
    public boolean support(Message message) {
        return true;
    }

    @Override
    public Collection<Connection> select(Message message, ConnectionRepository repository, ConnectionLoadBalanceConcept concept) {
        List<Connection> connections = null;
        for (ConnectionSelector selector : selectors) {
            if (!selector.support(message)) {
                continue;
            }
            Collection<Connection> select = selector.select(message, repository, concept);
            if (select == null || select.isEmpty()) {
                return select;
            }
            if (connections == null) {
                connections = new ArrayList<>(select);
            } else {
                connections.retainAll(select);
            }
            if (connections.isEmpty()) {
                return connections;
            }
        }
        return connections;
    }
}
