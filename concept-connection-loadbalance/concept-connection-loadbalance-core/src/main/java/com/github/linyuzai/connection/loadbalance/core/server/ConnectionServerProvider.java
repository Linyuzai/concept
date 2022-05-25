package com.github.linyuzai.connection.loadbalance.core.server;

import java.util.List;

/**
 * {@link ConnectionServer} 提供者
 */
public interface ConnectionServerProvider {

    ConnectionServer getClient();

    List<ConnectionServer> getConnectionServers();
}
