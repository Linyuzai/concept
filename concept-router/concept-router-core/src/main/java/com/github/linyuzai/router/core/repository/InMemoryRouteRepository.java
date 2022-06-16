package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.PathPatternRoute;
import com.github.linyuzai.router.core.concept.Route;
import com.github.linyuzai.router.core.exception.RouterException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRouteRepository implements RouteRepository {

    protected final Map<String, Route> routeMap = new ConcurrentHashMap<>();

    protected final Set<String> pathPatternSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void add(Collection<? extends Route> routes) {
        update(routes);
    }

    @Override
    public void update(Collection<? extends Route> routes) {
        for (Route route : routes) {
            PathPatternRoute r = (PathPatternRoute) route;
            if (pathPatternSet.contains(r.getPathPattern())) {
                throw new RouterException("Path pattern duplicated");
            }
            routeMap.put(route.getId(), route);
            pathPatternSet.add(r.getPathPattern());
        }
    }

    @Override
    public void remove(Collection<? extends String> ids) {
        for (String id : ids) {
            Route remove = routeMap.remove(id);
            if (remove == null) {
                continue;
            }
            PathPatternRoute r = (PathPatternRoute) remove;
            pathPatternSet.remove(r.getPathPattern());
        }
    }

    @Override
    public Route get(String id) {
        return routeMap.get(id);
    }

    @Override
    public List<Route> list(Collection<? extends String> ids) {
        List<Route> routes = new ArrayList<>();
        for (String id : ids) {
            Route route = routeMap.get(id);
            if (route == null) {
                continue;
            }
            routes.add(route);
        }
        return routes;
    }

    @Override
    public Collection<Route> all() {
        return Collections.unmodifiableCollection(routeMap.values());
    }
}
