package com.github.linyuzai.connection.loadbalance.core.server;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {@link ConnectionServer} 提供者默认实现
 */
public class ConnectionServerManagerImpl implements ConnectionServerManager {

    private final List<ConnectionServer> connectionServers = new CopyOnWriteArrayList<>();

    @Override
    public void add(ConnectionServer server) {
        connectionServers.add(server);
    }

    @Override
    public void remove(ConnectionServer server) {
        connectionServers.remove(server);
    }

    @Override
    public void clear() {
        connectionServers.clear();
    }

    @Override
    public ConnectionServer getLocal() {
        return null;
    }

    @Override
    public List<ConnectionServer> getConnectionServers() {
        return Collections.unmodifiableList(connectionServers);
    }
}
