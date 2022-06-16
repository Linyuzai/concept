package com.github.linyuzai.router.core.concept;

import com.github.linyuzai.router.core.locator.RouteLocator;
import com.github.linyuzai.router.core.matcher.RouteMatcher;
import com.github.linyuzai.router.core.repository.RouteRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class DefaultRouterConcept implements RouterConcept {

    private RouteRepository repository;

    private RouteMatcher matcher;

    private RouteLocator locator;

    @Override
    public Route.Location route(Route.Source source, Collection<? extends Route.Location> locations) {
        Route route = matcher.match(source, repository.all());
        if (route == null) {
            return null;
        }
        return locator.locate(route, locations);
    }
}
