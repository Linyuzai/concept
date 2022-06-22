package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.concept.ServiceRequestRouter;
import com.github.linyuzai.router.core.concept.ServiceRouterLocation;

import java.util.Collection;

public abstract class ServiceRequestRouterLocator extends AbstractRouterLocator<ServiceRequestRouter, ServiceRouterLocation> {

    @Override
    public Router.Location doLocate(ServiceRequestRouter router, Collection<? extends ServiceRouterLocation> locations) {
        for (ServiceRouterLocation location : locations) {
            if (matchServiceId(location, router) &&
                    matchHost(location, router) &&
                    matchPost(location, router)) {
                return location;
            }
        }
        if (router.isForced()) {
            return getForcedLocation();
        }
        return Router.Location.UNMATCHED;
    }

    public boolean matchServiceId(ServiceRouterLocation location, ServiceRequestRouter router) {
        return "*".equals(router.getServiceId()) ||
                location.getServiceId().equalsIgnoreCase(router.getServiceId());
    }

    public boolean matchHost(ServiceRouterLocation location, ServiceRequestRouter router) {
        return location.getHost().equals(router.getHost());
    }

    public boolean matchPost(ServiceRouterLocation location, ServiceRequestRouter router) {
        return "*".equals(router.getPort()) ||
                location.getPort() == Integer.parseInt(router.getPort());
    }

    public abstract Router.Location getForcedLocation();
}
