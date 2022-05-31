package com.github.linyuzai.connection.loadbalance.autoconfigure.discovery;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于 Spring Cloud 服务发现的服务实例提供者
 */
@Getter
public class DiscoveryConnectionServerManager implements ConnectionServerManager {

    private final DiscoveryClient discoveryClient;

    private final Registration registration;

    /**
     * 本服务信息
     */
    private final ConnectionServer local;

    public DiscoveryConnectionServerManager(DiscoveryClient discoveryClient, Registration registration) {
        this.discoveryClient = discoveryClient;
        this.registration = registration;
        this.local = new ServiceInstanceConnectionServer(registration);
    }

    @Override
    public void add(ConnectionServer server) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(ConnectionServer server) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEqual(ConnectionServer server1, ConnectionServer server2) {
        return server1.getHost().equals(server2.getHost()) && server1.getPort() == server2.getPort();
    }

    /**
     * 获得所有除自身外的服务实例
     *
     * @return 所有除自身外的服务实例
     */
    @Override
    public List<ConnectionServer> getConnectionServers() {
        List<ConnectionServer> servers = new ArrayList<>();
        List<ServiceInstance> instances = discoveryClient.getInstances(registration.getServiceId());
        for (ServiceInstance instance : instances) {
            ConnectionServer server = newConnectionServer(instance);
            if (isEqual(local, server)) {
                continue;
            }
            servers.add(server);
        }
        return servers;
    }

    public ConnectionServer newConnectionServer(ServiceInstance instance) {
        return new ServiceInstanceConnectionServer(instance);
    }

    @AllArgsConstructor
    public static class ServiceInstanceConnectionServer implements ConnectionServer {

        private ServiceInstance instance;

        @Override
        public String getInstanceId() {
            return instance.getInstanceId();
        }

        @Override
        public String getServiceId() {
            return instance.getServiceId();
        }

        @Override
        public String getHost() {
            return instance.getHost();
        }

        @Override
        public int getPort() {
            return instance.getPort();
        }

        @Override
        public Map<String, String> getMetadata() {
            return instance.getMetadata();
        }

        @Override
        public URI getUri() {
            return instance.getUri();
        }

        @Override
        public String getScheme() {
            return instance.getScheme();
        }

        @Override
        public boolean isSecure() {
            return instance.isSecure();
        }
    }
}
