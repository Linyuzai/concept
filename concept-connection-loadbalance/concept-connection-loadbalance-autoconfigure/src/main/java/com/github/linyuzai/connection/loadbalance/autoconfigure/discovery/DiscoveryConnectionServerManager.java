package com.github.linyuzai.connection.loadbalance.autoconfigure.discovery;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
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
 * 基于 Spring Cloud 服务发现的连接服务管理器。
 * 在使用 ws 双向连接时，通过 {@link DiscoveryClient} 获得其他服务的地址和端口进行连接。
 * <p>
 * {@link ConnectionServerManager} impl by Spring Cloud Service Discovery.
 * Get host and port from {@link DiscoveryClient} to connect other instances when use
 * ws-bidirectional-connection to forward message.
 */
@Getter
public class DiscoveryConnectionServerManager implements ConnectionServerManager {

    private final DiscoveryClient discoveryClient;

    private final Registration registration;

    /**
     * 本服务信息
     * <p>
     * This server's info
     */
    private final ConnectionServer local;

    public DiscoveryConnectionServerManager(DiscoveryClient discoveryClient, Registration registration) {
        this.discoveryClient = discoveryClient;
        this.registration = registration;
        this.local = new ServiceInstanceConnectionServer(registration);
    }

    @Override
    public ConnectionServer getLocal(ConnectionLoadBalanceConcept concept) {
        return local;
    }

    /**
     * 获得所有除自身外的服务实例
     * <p>
     * Get all instances except itself
     */
    @Override
    public List<ConnectionServer> getConnectionServers(ConnectionLoadBalanceConcept concept) {
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
