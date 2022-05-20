package com.github.linyuzai.connection.loadbalance.core.repository;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultConnectionRepository implements ConnectionRepository {

    protected final Map<String, Map<Object, Connection>> connections = new ConcurrentHashMap<>();

    @Override
    public Connection get(Object id, String type) {
        return connections.getOrDefault(type, Collections.emptyMap()).get(id);
    }

    @Override
    public Collection<Connection> select(String type) {
        return Collections.unmodifiableCollection(connections.getOrDefault(type, Collections.emptyMap()).values());
    }

    @Override
    public Collection<Connection> all() {
        return Collections.unmodifiableCollection(stream().collect(Collectors.toList()));
    }

    @Override
    public void add(Connection connection) {
        connections.computeIfAbsent(connection.getType(), type -> new ConcurrentHashMap<>())
                .put(connection.getId(), connection);
    }

    @Override
    public Connection remove(Connection connection) {
        return remove(connection.getId(), connection.getType());
    }

    @Override
    public Connection remove(Object id, String type) {
        return connections.getOrDefault(type, Collections.emptyMap()).remove(id);
    }

    @Override
    public Stream<Connection> stream() {
        return connections.values()
                .stream()
                .flatMap(it -> it.values().stream());
    }

    @Override
    public Stream<Connection> stream(String type) {
        return connections.getOrDefault(type, Collections.emptyMap()).values().stream();
    }
}
