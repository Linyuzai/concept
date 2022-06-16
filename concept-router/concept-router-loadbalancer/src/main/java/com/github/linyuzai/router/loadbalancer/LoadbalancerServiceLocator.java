package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.PathPatternRoute;
import com.github.linyuzai.router.core.concept.Route;
import com.github.linyuzai.router.core.locator.RouteLocator;
import com.github.linyuzai.router.loadbalancer.ServiceInstanceRouteLocation;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collection;

public class LoadbalancerServiceLocator implements RouteLocator {

    @Override
    public Route.Location locate(Route route, Collection<? extends Route.Location> locations) {
        PathPatternRoute pathPatternRoute = (PathPatternRoute) route;
        for (Route.Location location : locations) {
            ServiceInstanceRouteLocation routeLocation = (ServiceInstanceRouteLocation) location;
            ServiceInstance serviceInstance = routeLocation.getServiceInstance();
            if (matchServiceId(serviceInstance, pathPatternRoute) &&
                    matchHost(serviceInstance, pathPatternRoute) &&
                    matchPost(serviceInstance, pathPatternRoute)) {
                return location;
            }
        }
        if (pathPatternRoute.isForce()) {
            return new ServiceInstanceRouteLocation(null);
        }
        return null;
    }

    public boolean matchServiceId(ServiceInstance server, PathPatternRoute router) {
        return "*".equals(router.getServiceId()) ||
                "**".equals(router.getServiceId()) ||
                server.getServiceId().equals(router.getServiceId());
    }

    public boolean matchHost(ServiceInstance server, PathPatternRoute router) {
        return "*".equals(router.getHost()) ||
                "**".equals(router.getHost()) ||
                server.getHost().equals(router.getHost());
    }

    public boolean matchPost(ServiceInstance server, PathPatternRoute router) {
        return "*".equals(router.getPort()) ||
                "**".equals(router.getPort()) ||
                server.getPort() == Integer.parseInt(router.getPort());
    }
}
