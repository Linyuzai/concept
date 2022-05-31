package com.github.linyuzai.connection.loadbalance.core.server;

import java.util.List;

/**
 * {@link ConnectionServer} 提供者
 */
public interface ConnectionServerManager {

    void add(ConnectionServer server);

    void remove(ConnectionServer server);

    void clear();

    boolean isEqual(ConnectionServer server1, ConnectionServer server2);

    ConnectionServer getLocal();

    List<ConnectionServer> getConnectionServers();
}
