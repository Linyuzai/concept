package com.github.linyuzai.connection.loadbalance.core.server;

/**
 * {@link SimpleConnectionServerManager} 工厂。
 * <p>
 * Factory of {@link SimpleConnectionServerManager}.
 */
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
