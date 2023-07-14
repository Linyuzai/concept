package com.github.linyuzai.connection.loadbalance.core.repository;

/**
 * {@link TypeGroupedConnectionRepository} 的连接仓库工厂。
 * <p>
 * Factory of {@link TypeGroupedConnectionRepository}.
 */
public class TypeGroupedConnectionRepositoryFactory implements ConnectionRepositoryFactory {

    @Override
    public ConnectionRepository create(String scope) {
        return new TypeGroupedConnectionRepository();
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
