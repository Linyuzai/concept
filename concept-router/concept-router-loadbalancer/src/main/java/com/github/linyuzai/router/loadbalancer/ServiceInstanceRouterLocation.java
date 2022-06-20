package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.AbstractRouter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;

@Getter
@AllArgsConstructor
public class ServiceInstanceRouterLocation extends AbstractRouter.Location {

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
