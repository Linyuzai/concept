package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;

public class DefaultServiceRouterLocator extends ServiceRequestRouterLocator {

    @Override
    public Router.Location getLocationIfUnavailable() {
        return Router.Location.UNMATCHED;
    }
}
