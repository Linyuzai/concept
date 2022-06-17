package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.Router;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;

@Getter
@AllArgsConstructor
public class ServiceInstanceRouterLocation implements Router.Location {

    private ServiceInstance serviceInstance;
}
