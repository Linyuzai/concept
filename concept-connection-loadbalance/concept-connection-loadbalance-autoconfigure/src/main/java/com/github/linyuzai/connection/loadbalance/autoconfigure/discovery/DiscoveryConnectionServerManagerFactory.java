package com.github.linyuzai.connection.loadbalance.autoconfigure.discovery;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;

@Getter
@RequiredArgsConstructor
public class DiscoveryConnectionServerManagerFactory implements ConnectionServerManagerFactory {

    private final DiscoveryClient discoveryClient;

    private final Registration registration;

    @Override
    public ConnectionServerManager create(String scope) {
        return new DiscoveryConnectionServerManager(discoveryClient, registration);
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
