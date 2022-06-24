package com.github.linyuzai.router.ribbon.gateway.v2;

import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.ribbon.RouterLoadBalancerClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.Objects;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

public class RouterLoadBalancerClientFilterV2 extends LoadBalancerClientFilter {

    private final ApplicationContext context;

    private final RouterConcept concept;

    public RouterLoadBalancerClientFilterV2(ApplicationContext context, RouterConcept concept) {
        super(context.getBean(LoadBalancerClient.class), context.getBean(LoadBalancerProperties.class));
        this.context = context;
        this.concept = concept;
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String serviceId = Objects.requireNonNull(uri).getHost();
        return getRouterLoadBalancerClient().choose(serviceId, exchange);
    }

    protected RouterLoadBalancerClient getRouterLoadBalancerClient() {
        if (loadBalancer instanceof RouterLoadBalancerClient) {
            return (RouterLoadBalancerClient) loadBalancer;
        }
        return new RouterLoadBalancerClient(context, loadBalancer, concept);
    }
}
