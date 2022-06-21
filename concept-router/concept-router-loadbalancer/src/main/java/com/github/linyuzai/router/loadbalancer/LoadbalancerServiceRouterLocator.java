package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.ServiceRouterLocation;
import com.github.linyuzai.router.core.locator.ServiceRequestRouterLocator;

public class LoadbalancerServiceRouterLocator extends ServiceRequestRouterLocator {

    @Override
    public ServiceRouterLocation getForcedLocation() {
        return new LoadbalancerServiceRouterLocation(null);
    }
}
