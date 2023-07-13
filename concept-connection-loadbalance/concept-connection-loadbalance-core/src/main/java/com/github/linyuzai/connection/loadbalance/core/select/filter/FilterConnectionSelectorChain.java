package com.github.linyuzai.connection.loadbalance.core.select.filter;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FilterConnectionSelectorChain extends AbstractScoped implements ConnectionSelector {

    private final List<FilterConnectionSelector> selectors;

    @Override
    public boolean support(Message message, ConnectionLoadBalanceConcept concept) {
        return true;
    }

    @Override
    public Collection<Connection> select(Message message, ConnectionLoadBalanceConcept concept) {
        List<Connection> connections = null;
        for (ConnectionSelector selector : selectors) {
            if (!selector.support(message, concept)) {
                continue;
            }
            Collection<Connection> select = selector.select(message, concept);
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
