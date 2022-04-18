package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

public interface ConnectionProxy {
    String ENDPOINT_PREFIX = "/concept-ws-proxy/";
    String HEADER = "proxy";
    Connection proxy(ConnectionServer server);


}
