package com.github.linyuzai.connection.loadbalance.core.server;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;

public class ConnectionServerManagerFactoryImpl extends AbstractScopedFactory<ConnectionServerManager>
        implements ConnectionServerManagerFactory {

    @Override
    public ConnectionServerManager create(String scope) {
        return new ConnectionServerManagerImpl();
    }
}
