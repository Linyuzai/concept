package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractRouterLocator<R extends Router, L extends Router.Location> implements RouterLocator {

    @SuppressWarnings("unchecked")
    @Override
    public Router.Location locate(Router router, Collection<? extends Router.Location> locations) {
        return doLocate((R) router, locations.stream().map(it -> (L) it).collect(Collectors.toList()));
    }

    public abstract Router.Location doLocate(R router, Collection<? extends L> locations);
}
