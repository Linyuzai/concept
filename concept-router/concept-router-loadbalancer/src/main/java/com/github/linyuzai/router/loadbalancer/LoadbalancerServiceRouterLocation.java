package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.ServiceRouterLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;

@Getter
@AllArgsConstructor
public class LoadbalancerServiceRouterLocation implements ServiceRouterLocation {

    private ServiceInstance serviceInstance;

    @Override
    public String getServiceId() {
        return serviceInstance == null ? null : serviceInstance.getServiceId();
    }

    @Override
    public String getHost() {
        return serviceInstance == null ? null : serviceInstance.getHost();
    }

    @Override
    public int getPort() {
        return serviceInstance == null ? -1 : serviceInstance.getPort();
    }
}
