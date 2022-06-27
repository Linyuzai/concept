package com.github.linyuzai.router.ribbon;

import com.github.linyuzai.router.core.concept.RouterConcept;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.netflix.ribbon.*;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URI;

public class RouterLoadBalancerClient extends RibbonLoadBalancerClient {

    private final LoadBalancerClient client;

    public RouterLoadBalancerClient(ApplicationContext context, LoadBalancerClient client) {
        super(context.getBean(SpringClientFactory.class));
        this.client = client;
    }

    @Override
    public ServiceInstance choose(String serviceId, Object hint) {
        if (client instanceof RibbonLoadBalancerClient) {
            return ((RibbonLoadBalancerClient) client).choose(serviceId, hint);
        } else {
            return super.choose(serviceId, hint);
        }
    }

    @Override
    public URI reconstructURI(ServiceInstance instance, URI original) {
        return client.reconstructURI(instance, original);
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        return client.choose(serviceId);
    }

    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
        return client.execute(serviceId, request);
    }

    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request, Object hint) throws IOException {
        if (client instanceof RibbonLoadBalancerClient) {
            return ((RibbonLoadBalancerClient) client).execute(serviceId, request, hint);
        } else {
            return super.execute(serviceId, request, hint);
        }
    }

    @Override
    public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request) throws IOException {
        return client.execute(serviceId, serviceInstance, request);
    }
}
