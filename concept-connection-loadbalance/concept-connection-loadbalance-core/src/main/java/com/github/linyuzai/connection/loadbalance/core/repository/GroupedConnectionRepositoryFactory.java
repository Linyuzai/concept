package com.github.linyuzai.connection.loadbalance.core.repository;

import lombok.Getter;

@Getter
public class GroupedConnectionRepositoryFactory implements ConnectionRepositoryFactory {

    @Override
    public ConnectionRepository create(String scope) {
        return new GroupedConnectionRepository();
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
