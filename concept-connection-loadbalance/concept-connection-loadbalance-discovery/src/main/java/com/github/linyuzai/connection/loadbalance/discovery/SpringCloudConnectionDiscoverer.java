package com.github.linyuzai.connection.loadbalance.discovery;

import com.github.linyuzai.connection.loadbalance.core.discovery.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public abstract class SpringCloudConnectionDiscoverer implements ConnectionDiscoverer {

    private DiscoveryClient discoveryClient;

    private Registration registration;

    private DiscoveryConnectionFactory factory;

    @Override
    public DiscoveryConnection discover() {
        Collection<DiscoveryConnection> connections = new ArrayList<>();
        List<ServiceInstance> instances = discoveryClient.getInstances(registration.getServiceId());
        for (ServiceInstance instance : instances) {
            if (registration.getInstanceId().equals(instance.getInstanceId())) {
                continue;
            }
            DiscoveryConnection connection = factory.create(getHost(instance), getPort(instance));
            connection.send(new DiscoveryMessage(registration.getHost(), registration.getPort()));
            connections.add(connection);
        }
        return new DiscoveryConnections(connections);
    }

    public String getHost(ServiceInstance instance) {
        String host = instance.getMetadata().get(getHostKey());
        if (StringUtils.hasText(host)) {
            return host;
        }
        return instance.getHost();
    }

    public int getPort(ServiceInstance instance) {
        String port = instance.getMetadata().get(getPortKey());
        try {
            return Integer.parseInt(port);
        } catch (Throwable ignore) {
            return instance.getPort();
        }
    }

    public abstract String getHostKey();

    public abstract String getPortKey();
}
