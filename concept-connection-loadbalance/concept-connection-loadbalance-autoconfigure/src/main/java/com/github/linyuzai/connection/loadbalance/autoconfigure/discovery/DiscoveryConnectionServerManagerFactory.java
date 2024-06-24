package com.github.linyuzai.connection.loadbalance.autoconfigure.discovery;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;

/**
 * 基于服务发现的服务管理器工厂。
 * <p>
 * Factory of connection server manager based on Spring Cloud Discovery.
 *
 * @see DiscoveryConnectionServerManager
 */
@Getter
@Setter
public class DiscoveryConnectionServerManagerFactory implements ConnectionServerManagerFactory {

    private DiscoveryClient discoveryClient;

    private Registration registration;

    @Override
    public ConnectionServerManager create(String scope) {
        return new DiscoveryConnectionServerManager(discoveryClient, registration);
    }

    /**
     * 支持所有连接域。
     * <p>
     * Support all connection scope.
     *
     * @see Scoped
     */
    @Override
    public boolean support(String scope) {
        return true;
    }
}
