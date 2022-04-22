package com.github.linyuzai.connection.loadbalance.core.subscribe;

public interface ProxyMarker {

    String FLAG = "concept-connection-proxy";

    boolean hasProxyFlag();
}
