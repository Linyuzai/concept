package com.github.linyuzai.connection.loadbalance.discovery;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.discovery.ConnectionDiscoverer;
import com.github.linyuzai.connection.loadbalance.core.discovery.DiscoveryConnection;
import com.github.linyuzai.connection.loadbalance.core.discovery.DiscoveryConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.discovery.DiscoveryConnections;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpringCloudConnectionDiscoverer implements ConnectionDiscoverer {

    private DiscoveryClient discoveryClient;

    private Registration registration;

    private DiscoveryConnectionFactory factory;

    @Override
    public DiscoveryConnection discover() {
        Collection<DiscoveryConnection> connections = new ArrayList<>();
        List<ServiceInstance> instances = discoveryClient.getInstances(registration.getServiceId());
        for (ServiceInstance instance : instances) {
            if (registration.getHost().equals(instance.getHost()) &&
                    registration.getPort() == instance.getPort()) {
                continue;
            }
            DiscoveryConnection connection = factory.create(getHost(instance), getPort(instance));
            connections.add(connection);
        }
        return new DiscoveryConnections(connections);
    }

    public String getHost(ServiceInstance instance) {
        String host = instance.getMetadata().get("host");
        if (StringUtils.hasText(host)) {
            return host;
        }
        return instance.getHost();
    }

    public int getPort(ServiceInstance instance) {
        String port = instance.getMetadata().get("port");
        try {
            return Integer.parseInt(port);
        } catch (Throwable ignore) {
            return instance.getPort();
        }
    }
}
