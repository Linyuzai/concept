package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.locator.RouterLocator;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collection;

public class LoadbalancerServiceLocator implements RouterLocator {

    @Override
    public Router.Location locate(Router router, Collection<? extends Router.Location> locations) {
        PathPatternRouter pathPatternRoute = (PathPatternRouter) router;
        for (Router.Location location : locations) {
            ServiceInstanceRouterLocation routeLocation = (ServiceInstanceRouterLocation) location;
            ServiceInstance serviceInstance = routeLocation.getServiceInstance();
            if (matchServiceId(serviceInstance, pathPatternRoute) &&
                    matchHost(serviceInstance, pathPatternRoute) &&
                    matchPost(serviceInstance, pathPatternRoute)) {
                return location;
            }
        }
        if (pathPatternRoute.isForced()) {
            return new ServiceInstanceRouterLocation(null);
        }
        return null;
    }

    public boolean matchServiceId(ServiceInstance server, PathPatternRouter router) {
        return "*".equals(router.getServiceId()) ||
                server.getServiceId().equalsIgnoreCase(router.getServiceId());
    }

    public boolean matchHost(ServiceInstance server, PathPatternRouter router) {
        return server.getHost().equals(router.getHost());
    }

    public boolean matchPost(ServiceInstance server, PathPatternRouter router) {
        return "*".equals(router.getPort()) ||
                server.getPort() == Integer.parseInt(router.getPort());
    }
}
