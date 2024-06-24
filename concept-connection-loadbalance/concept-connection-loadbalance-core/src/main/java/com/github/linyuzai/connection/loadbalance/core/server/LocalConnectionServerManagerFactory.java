package com.github.linyuzai.connection.loadbalance.core.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@link LocalConnectionServerManager} 工厂。
 * <p>
 * Factory of {@link LocalConnectionServerManager}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalConnectionServerManagerFactory implements ConnectionServerManagerFactory {

    private ConnectionServer local;

    @Override
    public ConnectionServerManager create(String scope) {
        return new LocalConnectionServerManager(local);
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
