package com.github.linyuzai.connection.loadbalance.core.proxy;

public interface ProxyMarker {

    String FLAG = "concept-connection-proxy";

    boolean hasProxyFlag();
}
