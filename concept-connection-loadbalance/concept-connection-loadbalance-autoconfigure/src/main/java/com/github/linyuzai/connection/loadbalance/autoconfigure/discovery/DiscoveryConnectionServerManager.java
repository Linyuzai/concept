package com.github.linyuzai.connection.loadbalance.autoconfigure.discovery;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于 Spring Cloud 服务发现的服务管理器。
 * 在使用双向连接转发消息时，通过 {@link DiscoveryClient} 获得其他服务的地址和端口进行连接。
 * <p>
 * Connection server manager impl by Spring Cloud Discovery.
 * Get host and port from {@link DiscoveryClient} to connect other instances when using
 * bidirectional-connection to forward message.
 *
 * @see ConnectionServerManager
 */
@Getter
public class DiscoveryConnectionServerManager implements ConnectionServerManager {

    private final DiscoveryClient discoveryClient;

    private final Registration registration;

    /**
     * 本服务信息
     * <p>
     * Info of the local server
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
     * 获得所有除自身外的服务实例。
     * <p>
     * Get all instances except itself.
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

}
