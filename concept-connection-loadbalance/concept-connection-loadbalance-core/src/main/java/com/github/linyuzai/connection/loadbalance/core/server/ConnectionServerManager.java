package com.github.linyuzai.connection.loadbalance.core.server;

import java.util.List;

/**
 * {@link ConnectionServer} 提供者
 */
public interface ConnectionServerManager {

    default void add(ConnectionServer server) {
        throw new UnsupportedOperationException();
    }

    default void remove(ConnectionServer server) {
        throw new UnsupportedOperationException();
    }

    default void clear() {
        throw new UnsupportedOperationException();
    }

    default boolean isEqual(ConnectionServer server1, ConnectionServer server2) {
        return server1.getHost().equals(server2.getHost()) && server1.getPort() == server2.getPort();
    }

    ConnectionServer getLocal();

    List<ConnectionServer> getConnectionServers();
}
