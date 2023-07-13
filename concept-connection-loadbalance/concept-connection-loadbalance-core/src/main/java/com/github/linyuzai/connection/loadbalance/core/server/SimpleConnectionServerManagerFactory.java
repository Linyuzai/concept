package com.github.linyuzai.connection.loadbalance.core.server;

public class SimpleConnectionServerManagerFactory implements ConnectionServerManagerFactory {

    @Override
    public ConnectionServerManager create(String scope) {
        return new SimpleConnectionServerManager();
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
