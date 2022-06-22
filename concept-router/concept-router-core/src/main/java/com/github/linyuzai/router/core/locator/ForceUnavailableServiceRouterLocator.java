package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;

public class ForceUnavailableServiceRouterLocator extends ServiceRequestRouterLocator {

    @Override
    public Router.Location getForcedLocation() {
        return Router.Location.UNAVAILABLE;
    }
}
