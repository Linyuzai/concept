package com.github.linyuzai.connection.loadbalance.core.server;

import java.util.List;

public interface ConnectionServerProvider {

    ConnectionServer getClient();

    List<ConnectionServer> getConnectionServers();


}
