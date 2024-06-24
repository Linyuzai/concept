package com.github.linyuzai.connection.loadbalance.core.server;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 服务管理器。
 * 用于管理同步消息的服务。
 * <p>
 * Management of connectable server which need sync message.
 */
public interface ConnectionServerManager {

    /**
     * 添加服务。
     * <p>
     * Add a server.
     */
    default void add(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        throw new UnsupportedOperationException();
    }

    /**
     * 添加服务。
     * <p>
     * Add a server.
     */
    default void add(ConnectionServer server) {
        add(server, null);
    }

    /**
     * 移除服务。
     * <p>
     * Remove a server.
     */
    default void remove(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        throw new UnsupportedOperationException();
    }

    /**
     * 移除服务。
     * <p>
     * Remove a server.
     */
    default void remove(ConnectionServer server) {
        remove(server, null);
    }

    /**
     * 清空服务。
     * <p>
     * Clear servers.
     */
    default void clear(ConnectionLoadBalanceConcept concept) {
        throw new UnsupportedOperationException();
    }

    /**
     * 清空服务。
     * <p>
     * Clear servers.
     */
    default void clear() {
        clear(null);
    }

    /**
     * 比较两个服务是否相等。
     * <p>
     * Compare whether two services are equal.
     */
    default boolean isEqual(ConnectionServer server1, ConnectionServer server2, ConnectionLoadBalanceConcept concept) {
        return server1.getHost().equals(server2.getHost()) && server1.getPort() == server2.getPort();
    }

    /**
     * 比较两个服务是否相等。
     * <p>
     * Compare whether two services are equal.
     */
    default boolean isEqual(ConnectionServer server1, ConnectionServer server2) {
        return isEqual(server1, server2, null);
    }

    /**
     * 获得本地服务。
     * <p>
     * Get local server.
     */
    ConnectionServer getLocal(ConnectionLoadBalanceConcept concept);

    /**
     * 获得本地服务。
     * <p>
     * Get local server.
     */
    default ConnectionServer getLocal() {
        return getLocal(null);
    }

    /**
     * 获得需要连接的服务。
     * 当使用服务间连接来转发消息的时被调用。
     * <p>
     * Get servers need connect when using bidirectional-connection to forward message.
     */
    List<ConnectionServer> getConnectionServers(ConnectionLoadBalanceConcept concept);

    /**
     * 获得需要连接的服务。
     * <p>
     * Get servers need connect when using bidirectional-connection to forward message.
     */
    default List<ConnectionServer> getConnectionServers() {
        return getConnectionServers(null);
    }

    /**
     * 连接服务管理器代理。
     * <p>
     * Delegate of server manager.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements ConnectionServerManager {

        private final ConnectionLoadBalanceConcept concept;

        private final ConnectionServerManager delegate;

        public static ConnectionServerManager delegate(ConnectionLoadBalanceConcept concept,
                                                       ConnectionServerManager delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public void add(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
            delegate.add(server, concept);
        }

        @Override
        public void add(ConnectionServer server) {
            delegate.add(server, concept);
        }

        @Override
        public void remove(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
            delegate.remove(server, concept);
        }

        @Override
        public void remove(ConnectionServer server) {
            delegate.remove(server, concept);
        }

        @Override
        public void clear(ConnectionLoadBalanceConcept concept) {
            delegate.clear(concept);
        }

        @Override
        public void clear() {
            delegate.clear(concept);
        }

        @Override
        public boolean isEqual(ConnectionServer server1, ConnectionServer server2, ConnectionLoadBalanceConcept concept) {
            return delegate.isEqual(server1, server2, concept);
        }

        @Override
        public boolean isEqual(ConnectionServer server1, ConnectionServer server2) {
            return delegate.isEqual(server1, server2, concept);
        }

        @Override
        public ConnectionServer getLocal(ConnectionLoadBalanceConcept concept) {
            return delegate.getLocal(concept);
        }

        @Override
        public ConnectionServer getLocal() {
            return delegate.getLocal(concept);
        }

        @Override
        public List<ConnectionServer> getConnectionServers(ConnectionLoadBalanceConcept concept) {
            return delegate.getConnectionServers(concept);
        }

        @Override
        public List<ConnectionServer> getConnectionServers() {
            return delegate.getConnectionServers(concept);
        }
    }
}
