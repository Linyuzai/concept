package com.github.linyuzai.connection.loadbalance.core.server;

public class ConnectionServerManagerFactoryImpl implements ConnectionServerManagerFactory {

    @Override
    public ConnectionServerManager create(String scope) {
        return new ConnectionServerManagerImpl();
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
