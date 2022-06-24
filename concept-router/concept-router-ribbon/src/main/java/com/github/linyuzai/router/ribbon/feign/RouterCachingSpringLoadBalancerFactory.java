package com.github.linyuzai.router.ribbon.feign;

import com.github.linyuzai.router.core.concept.RouterConcept;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;

public class RouterCachingSpringLoadBalancerFactory extends CachingSpringLoadBalancerFactory {

    private final CachingSpringLoadBalancerFactory factory;

    private final RouterConcept concept;

    public RouterCachingSpringLoadBalancerFactory(CachingSpringLoadBalancerFactory factory,
                                                  RouterConcept concept) {
        super(null);
        this.factory = factory;
        this.concept = concept;
    }

    @Override
    public FeignLoadBalancer create(String clientName) {
        return new RouterFeignLoadBalancer(factory.create(clientName), concept);
    }
}
