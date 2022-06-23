package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;

public class DefaultServiceRouterLocator extends ServiceRequestRouterLocator {

    @Override
    public Router.Location getForcedLocation() {
        return Router.Location.UNAVAILABLE;
    }
}
