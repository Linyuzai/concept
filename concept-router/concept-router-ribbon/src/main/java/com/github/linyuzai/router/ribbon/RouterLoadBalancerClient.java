package com.github.linyuzai.router.ribbon;

import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.concept.RouterConcept;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.netflix.ribbon.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.server.ServerWebExchange;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class RouterLoadBalancerClient extends RibbonLoadBalancerClient {

    private final LoadBalancerClient client;

    private final SpringClientFactory clientFactory;

    private final RouterConcept concept;

    public RouterLoadBalancerClient(ApplicationContext context, LoadBalancerClient client, RouterConcept concept) {
        super(context.getBean(SpringClientFactory.class));
        this.client = client;
        this.clientFactory = context.getBean(SpringClientFactory.class);
        this.concept = concept;
    }

    @Override
    public ServiceInstance choose(String serviceId, Object hint) {
        if (hint instanceof ServerWebExchange) {
            ILoadBalancer iLoadBalancer = getLoadBalancer(serviceId);
            List<Server> servers = iLoadBalancer.getReachableServers();
            List<RibbonServiceRouterLocation> locations = servers.stream()
                    .map(it -> new RibbonServiceRouterLocation(serviceId, it))
                    .collect(Collectors.toList());
            Router.Source source = new ServerWebExchangeRouterSource(serviceId, (ServerWebExchange) hint);
            Router.Location location = concept.route(source, locations);
            if (location == Router.Location.UNMATCHED) {
                return client.choose(serviceId);
            } else if (location == Router.Location.UNAVAILABLE) {
                return null;
            } else {
                RibbonServiceRouterLocation l = (RibbonServiceRouterLocation) location;
                Server server = l.getServer();
                return new RibbonServer(serviceId, server, isSecure(server, serviceId),
                        serverIntrospector(serviceId).getMetadata(server));
            }
        } else {
            if (client instanceof RibbonLoadBalancerClient) {
                return ((RibbonLoadBalancerClient) client).choose(serviceId, hint);
            } else {
                return super.choose(serviceId, hint);
            }
        }
    }

    private ServerIntrospector serverIntrospector(String serviceId) {
        ServerIntrospector serverIntrospector = this.clientFactory.getInstance(serviceId,
                ServerIntrospector.class);
        if (serverIntrospector == null) {
            serverIntrospector = new DefaultServerIntrospector();
        }
        return serverIntrospector;
    }

    private boolean isSecure(Server server, String serviceId) {
        IClientConfig config = this.clientFactory.getClientConfig(serviceId);
        ServerIntrospector serverIntrospector = serverIntrospector(serviceId);
        return RibbonUtils.isSecure(config, serverIntrospector, server);
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
