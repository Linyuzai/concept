package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;

@Getter
@AllArgsConstructor
public class ServiceInstanceRouteLocation implements Route.Location {

    private ServiceInstance serviceInstance;
}
