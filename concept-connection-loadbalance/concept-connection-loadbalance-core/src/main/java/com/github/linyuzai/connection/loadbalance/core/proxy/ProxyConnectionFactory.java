package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;

import java.util.Map;

public abstract class ProxyConnectionFactory implements ConnectionFactory {

    @Override
    public boolean support(Object o, Map<String, String> metadata) {
        return metadata.containsKey(ConnectionProxy.FLAG) && support(o);
    }

    public abstract boolean support(Object o);
}
