package com.github.linyuzai.router.ribbon.gateway.v1;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.netflix.ribbon.*;
import org.springframework.context.ApplicationContext;

import java.net.URI;

public class RouterLoadBalancerClient extends RibbonLoadBalancerClient {

    private final URI uri;

    public RouterLoadBalancerClient(ApplicationContext context, URI uri) {
        super(context.getBean(SpringClientFactory.class));
        this.uri = uri;
    }

    @Override
    protected Server getServer(ILoadBalancer loadBalancer) {
        return getServer(loadBalancer, uri);
    }

    @Override
    protected Server getServer(ILoadBalancer loadBalancer, Object hint) {
        if (loadBalancer == null) {
            return null;
        }
        return loadBalancer.chooseServer(uri);
    }
}
