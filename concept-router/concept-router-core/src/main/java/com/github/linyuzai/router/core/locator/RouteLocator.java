package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Route;

import java.util.Collection;

public interface RouteLocator {

    Route.Location locate(Route route, Collection<? extends Route.Location> locations);
}
