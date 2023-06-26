package com.github.linyuzai.connection.loadbalance.core.repository;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * 连接仓库
 */
public interface ConnectionRepository {

    /**
     * 获得一个连接
     *
     * @param id   连接 id
     * @param type 连接类型
     * @return 连接或 null
     */
    Connection get(Object id, String type, ConnectionLoadBalanceConcept concept);

    default Connection get(Object id, String type) {
        return get(id, type, null);
    }

    /**
     * 通过连接类型获得对应的连接集合
     *
     * @param type 连接类型
     * @return 对应的连接集合
     */
    Collection<Connection> select(String type, ConnectionLoadBalanceConcept concept);

    default Collection<Connection> select(String type) {
        return select(type, null);
    }

    /**
     * 获得所有连接类型
     *
     * @return 所有连接类型
     */
    Collection<String> types(ConnectionLoadBalanceConcept concept);

    default Collection<String> types() {
        return types(null);
    }

    /**
     * 添加连接
     *
     * @param connection 连接
     */
    void add(Connection connection, ConnectionLoadBalanceConcept concept);

    default void add(Connection connection) {
        add(connection, null);
    }

    /**
     * 移除连接
     *
     * @param connection 被移除的连接
     * @return 连接仓库中存在则返回对应的连接，否则返回 null
     */
    Connection remove(Connection connection, ConnectionLoadBalanceConcept concept);

    default Connection remove(Connection connection) {
        return remove(connection, null);
    }

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
