package com.github.linyuzai.connection.loadbalance.core.server;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * {@link ConnectionServer} 提供者
 */
public interface ConnectionServerManager {

    default void add(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        throw new UnsupportedOperationException();
    }

    default void add(ConnectionServer server) {
        add(server, null);
    }

    default void remove(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        throw new UnsupportedOperationException();
    }

    default void remove(ConnectionServer server) {
        remove(server, null);
    }

    default void clear(ConnectionLoadBalanceConcept concept) {
        throw new UnsupportedOperationException();
    }

    default void clear() {
        clear(null);
    }

    default boolean isEqual(ConnectionServer server1, ConnectionServer server2, ConnectionLoadBalanceConcept concept) {
        return server1.getHost().equals(server2.getHost()) && server1.getPort() == server2.getPort();
    }

    default boolean isEqual(ConnectionServer server1, ConnectionServer server2) {
        return isEqual(server1, server2, null);
    }

    ConnectionServer getLocal(ConnectionLoadBalanceConcept concept);

    default ConnectionServer getLocal() {
        return getLocal(null);
    }

    List<ConnectionServer> getConnectionServers(ConnectionLoadBalanceConcept concept);

    default List<ConnectionServer> getConnectionServers() {
        return getConnectionServers(null);
    }

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
