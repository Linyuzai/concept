package com.github.linyuzai.connection.loadbalance.core.repository;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的连接仓库
 */
public class GroupedConnectionRepository implements ConnectionRepository {

    protected final Map<String, Map<Object, Connection>> connections = new ConcurrentHashMap<>();

    @Override
    public Connection get(Object id, String type, ConnectionLoadBalanceConcept concept) {
        return connections.getOrDefault(type, Collections.emptyMap()).get(id);
    }

    @Override
    public Collection<Connection> select(String type, ConnectionLoadBalanceConcept concept) {
        return Collections.unmodifiableCollection(connections.getOrDefault(type, Collections.emptyMap()).values());
    }

    @Override
    public Collection<String> types(ConnectionLoadBalanceConcept concept) {
        return Collections.unmodifiableCollection(connections.keySet());
    }

    @Override
    public void add(Connection connection, ConnectionLoadBalanceConcept concept) {
        connections.computeIfAbsent(connection.getType(), type -> new ConcurrentHashMap<>())
                .put(connection.getId(), connection);
    }

    @Override
    public Connection remove(Connection connection, ConnectionLoadBalanceConcept concept) {
        return connections.getOrDefault(connection.getType(), Collections.emptyMap())
                .remove(connection.getId());
    }
}
