package com.github.linyuzai.router.ribbon.gateway.v2;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.Objects;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * 重写网关拦截器
 */
public class RouterLoadBalancerClientFilterV2 extends LoadBalancerClientFilter {

    public RouterLoadBalancerClientFilterV2(ApplicationContext context) {
        super(context.getBean(LoadBalancerClient.class), context.getBean(LoadBalancerProperties.class));
    }

    /**
     * 调用 {@link RibbonLoadBalancerClient#choose(String, Object)} 方法
     */
    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String serviceId = Objects.requireNonNull(uri).getHost();
        if (loadBalancer instanceof RibbonLoadBalancerClient) {
            return ((RibbonLoadBalancerClient) loadBalancer).choose(serviceId, uri);
        } else {
            return loadBalancer.choose(serviceId);
        }
    }
}
