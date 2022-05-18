package com.github.linyuzai.connection.loadbalance.core.repository;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.util.Collection;
import java.util.stream.Stream;

public interface ConnectionRepository {

    Connection get(Object id, String type);

    Collection<Connection> select(String type);

    Collection<Connection> all();

    void add(Connection connection);

    Connection remove(Connection connection);

    Connection remove(Object id, String type);

    Stream<Connection> stream();

    Stream<Connection> stream(String type);
}
