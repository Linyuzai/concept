package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.Route;

import java.util.Collection;

public interface RouteMatcher {

    Route match(Route.Source source, Collection<? extends Route> routes);
}
