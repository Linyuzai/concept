package com.github.linyuzai.connection.loadbalance.autoconfigure.discovery;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ServiceInstanceConnectionServer implements ConnectionServer {

    private final ServiceInstance instance;

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
