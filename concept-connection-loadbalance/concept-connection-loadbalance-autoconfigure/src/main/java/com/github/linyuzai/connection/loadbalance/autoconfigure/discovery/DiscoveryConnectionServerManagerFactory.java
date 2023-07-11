package com.github.linyuzai.connection.loadbalance.autoconfigure.discovery;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;

/**
 * {@link DiscoveryConnectionServerManager} 工厂。
 * <p>
 * Factory of {@link DiscoveryConnectionServerManager}.
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

    @Override
    public boolean support(String scope) {
        return true;
    }
}
