package com.github.linyuzai.router.ribbon.feign;

import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;

/**
 * {@link CachingSpringLoadBalancerFactory} 的路由扩展
 */
public class RouterCachingSpringLoadBalancerFactory extends CachingSpringLoadBalancerFactory {

    private final CachingSpringLoadBalancerFactory factory;

    public RouterCachingSpringLoadBalancerFactory(CachingSpringLoadBalancerFactory factory) {
        super(null);
        this.factory = factory;
    }

    /**
     * 将 {@link FeignLoadBalancer} 包装成 {@link RouterFeignLoadBalancer}
     *
     * @param clientName serviceId
     * @return {@link RouterFeignLoadBalancer}
     */
    @Override
    public FeignLoadBalancer create(String clientName) {
        return new RouterFeignLoadBalancer(factory.create(clientName));
    }
}
