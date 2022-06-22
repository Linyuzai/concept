package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.concept.RouterConcept;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class RouterReactorLoadbalancer implements ReactorServiceInstanceLoadBalancer {

    private final String serviceId;

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private final ReactiveLoadBalancer<ServiceInstance> loadBalancer;

    private final RouterConcept concept;

    static final Response<ServiceInstance> NOT_MATCH = new EmptyResponse();

    public RouterReactorLoadbalancer(
            String serviceId,
            LoadBalancerClientFactory factory,
            ReactiveLoadBalancer<ServiceInstance> loadBalancer,
            RouterConcept concept) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class);
        this.loadBalancer = loadBalancer;
        this.concept = concept;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request)
                .next()
                .map(it -> getInstanceResponse(request, it))
                .flatMap(it -> {
                    if (it == NOT_MATCH) {
                        return Mono.from(loadBalancer.choose(request));
                    } else {
                        return Mono.just(it);
                    }
                });
    }

    private Response<ServiceInstance> getInstanceResponse(Request<RequestDataContext> request, List<ServiceInstance> instances) {
        List<LoadbalancerServiceRouterLocation> locations = instances.stream()
                .map(LoadbalancerServiceRouterLocation::new)
                .collect(Collectors.toList());
        Router.Source source = new LoadbalancerRequestRouterSource(serviceId, request);
        Router.Location location = concept.route(source, locations);
        if (location == Router.Location.UNMATCHED) {
            return NOT_MATCH;
        } else if (location == Router.Location.UNAVAILABLE) {
            return new EmptyResponse();
        } else {
            LoadbalancerServiceRouterLocation l = (LoadbalancerServiceRouterLocation) location;
            ServiceInstance instance = l.getServiceInstance();
            return new DefaultResponse(instance);
        }
    }
}
