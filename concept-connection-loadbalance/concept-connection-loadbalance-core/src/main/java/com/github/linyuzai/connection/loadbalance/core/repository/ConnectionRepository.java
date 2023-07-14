package com.github.linyuzai.connection.loadbalance.core.repository;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * 连接仓库。
 * <p>
 * Repository to cache connections.
 */
public interface ConnectionRepository {

    /**
     * 获得一个连接。
     * <p>
     * Get a connection by id and type.
     */
    Connection get(Object id, String type, ConnectionLoadBalanceConcept concept);

    /**
     * 获得一个连接。
     * <p>
     * Get a connection by id and type.
     */
    default Connection get(Object id, String type) {
        return get(id, type, null);
    }

    /**
     * 通过连接类型获得对应的连接集合。
     * <p>
     * List connections by type.
     */
    Collection<Connection> select(String type, ConnectionLoadBalanceConcept concept);

    /**
     * 通过连接类型获得对应的连接集合。
     * <p>
     * List connections by type.
     */
    default Collection<Connection> select(String type) {
        return select(type, null);
    }

    /**
     * 获得所有连接类型。
     * <p>
     * Get all types.
     */
    Collection<String> types(ConnectionLoadBalanceConcept concept);

    /**
     * 获得所有连接类型。
     * <p>
     * Get all types.
     */
    default Collection<String> types() {
        return types(null);
    }

    /**
     * 添加连接。
     * <p>
     * Add a connection.
     */
    void add(Connection connection, ConnectionLoadBalanceConcept concept);

    /**
     * 添加连接。
     * <p>
     * Add a connection.
     */
    default void add(Connection connection) {
        add(connection, null);
    }

    /**
     * 移除连接。
     * <p>
     * Remove a connection.
     */
    Connection remove(Connection connection, ConnectionLoadBalanceConcept concept);

    /**
     * 移除连接。
     * <p>
     * Remove a connection.
     */
    default Connection remove(Connection connection) {
        return remove(connection, null);
    }

    /**
     * 连接仓库代理。
     * <p>
     * Delegate of connection's repository.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements ConnectionRepository {

        private final ConnectionLoadBalanceConcept concept;

        private final ConnectionRepository delegate;

        public static ConnectionRepository delegate(ConnectionLoadBalanceConcept concept,
                                                    ConnectionRepository delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public Connection get(Object id, String type, ConnectionLoadBalanceConcept concept) {
            return delegate.get(id, type, concept);
        }

        @Override
        public Connection get(Object id, String type) {
            return delegate.get(id, type, concept);
        }

        @Override
        public Collection<Connection> select(String type, ConnectionLoadBalanceConcept concept) {
            return delegate.select(type, concept);
        }

        @Override
        public Collection<Connection> select(String type) {
            return delegate.select(type, concept);
        }

        @Override
        public Collection<String> types(ConnectionLoadBalanceConcept concept) {
            return delegate.types(concept);
        }

        @Override
        public Collection<String> types() {
            return delegate.types(concept);
        }

        @Override
        public void add(Connection connection, ConnectionLoadBalanceConcept concept) {
            delegate.add(connection, concept);
        }

        @Override
        public void add(Connection connection) {
            delegate.add(connection, concept);
        }

        @Override
        public Connection remove(Connection connection, ConnectionLoadBalanceConcept concept) {
            return delegate.remove(connection, concept);
        }

        @Override
        public Connection remove(Connection connection) {
            return delegate.remove(connection, concept);
        }
    }
}
