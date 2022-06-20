package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.AbstractRouter;
import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.locator.RouterLocator;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collection;

public class LoadbalancerServiceLocator implements RouterLocator {

    @Override
    public Router.Location locate(Router router, Collection<? extends Router.Location> locations) {
        AbstractRouter ar = (AbstractRouter) router;
        for (Router.Location location : locations) {
            AbstractRouter.Location arl = (AbstractRouter.Location) location;
            if (matchServiceId(arl, ar) &&
                    matchHost(arl, ar) &&
                    matchPost(arl, ar)) {
                return location;
            }
        }
        if (ar.isForced()) {
            return new ServiceInstanceRouterLocation(null);
        }
        return null;
    }

    public boolean matchServiceId(AbstractRouter.Location location, AbstractRouter router) {
        return "*".equals(router.getServiceId()) ||
                location.getServiceId().equalsIgnoreCase(router.getServiceId());
    }

    public boolean matchHost(AbstractRouter.Location location, AbstractRouter router) {
        return location.getHost().equals(router.getHost());
    }

    public boolean matchPost(AbstractRouter.Location location, AbstractRouter router) {
        return "*".equals(router.getPort()) ||
                location.getPort() == Integer.parseInt(router.getPort());
    }
}
