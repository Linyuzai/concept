package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.subscribe.ProxyMarker;

import java.util.Map;

public interface Message extends ProxyMarker {
    String FORWARD = "concept-connection-forward";

    Map<String, String> getHeaders();

    <T> T getPayload();

    @Override
    default boolean hasProxyFlag() {
        return getHeaders().containsKey(ProxyMarker.FLAG);
    }
}
