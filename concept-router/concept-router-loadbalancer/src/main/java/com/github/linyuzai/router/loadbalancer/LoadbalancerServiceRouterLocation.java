package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.ServiceRouterLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.cloud.client.ServiceInstance;

/**
 * 基于 {@link ServiceInstance} 的路由位置
 */
@Getter
@AllArgsConstructor
public class LoadbalancerServiceRouterLocation implements ServiceRouterLocation {

    @NonNull
    private ServiceInstance serviceInstance;

    @Override
    public String getServiceId() {
        return serviceInstance.getServiceId();
    }

    @Override
    public String getHost() {
        return serviceInstance.getHost();
    }

    @Override
    public int getPort() {
        return serviceInstance.getPort();
    }
}
