package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;

import java.util.Map;

public abstract class AbstractConnectionFactory implements ConnectionFactory {

    @Override
    public boolean support(Object o, Map<String, String> metadata) {
        return !metadata.containsKey(ConnectionProxy.FLAG) && support(o);
    }

    public abstract boolean support(Object o);
}
