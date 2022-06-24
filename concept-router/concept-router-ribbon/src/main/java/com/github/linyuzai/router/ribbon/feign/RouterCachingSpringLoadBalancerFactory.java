package com.github.linyuzai.router.ribbon.feign;

import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;

public class RouterCachingSpringLoadBalancerFactory extends CachingSpringLoadBalancerFactory {

    private final CachingSpringLoadBalancerFactory factory;

    public RouterCachingSpringLoadBalancerFactory(CachingSpringLoadBalancerFactory factory) {
        super(null);
        this.factory = factory;
    }

    @Override
    public FeignLoadBalancer create(String clientName) {
        return new RouterFeignLoadBalancer(factory.create(clientName));
    }
}
