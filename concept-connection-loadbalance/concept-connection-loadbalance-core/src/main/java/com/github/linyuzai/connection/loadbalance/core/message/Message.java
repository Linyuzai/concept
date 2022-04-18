package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.proxy.ProxyMarker;

import java.util.Map;

public interface Message extends ProxyMarker {

    Map<String, String> getHeaders();

    <T> T getPayload();

    @Override
    default boolean isProxy() {
        return getHeaders().containsKey(ConnectionProxy.HEADER);
    }
}
