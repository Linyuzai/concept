package com.github.linyuzai.connection.loadbalance.core.repository;

import lombok.Getter;

@Getter
public class ConnectionRepositoryFactoryImpl implements ConnectionRepositoryFactory {

    @Override
    public ConnectionRepository create(String scope) {
        return new ConnectionRepositoryImpl();
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
