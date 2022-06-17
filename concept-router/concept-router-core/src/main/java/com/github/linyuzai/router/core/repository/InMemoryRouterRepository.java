package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.exception.RouterException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRouterRepository implements RouterRepository {

    protected final Map<String, Router> routeMap = new ConcurrentHashMap<>();

    protected final Map<String, Router> pathPatternMap = new ConcurrentHashMap<>();

    @Override
    public void add(Collection<? extends Router> routes) {
        update(routes);
    }

    @Override
    public void update(Collection<? extends Router> routes) {
        for (Router router : routes) {
            PathPatternRouter r = (PathPatternRouter) router;
            Router exist = pathPatternMap.get(r.getPathPattern());
            if (exist != null && !exist.getId().equals(r.getId())) {
                throw new RouterException("Path pattern duplicated");
            }
            routeMap.put(router.getId(), router);
            pathPatternMap.put(r.getPathPattern(), r);
        }
    }

    @Override
    public void remove(Collection<? extends String> ids) {
        for (String id : ids) {
            Router remove = routeMap.remove(id);
            if (remove == null) {
                continue;
            }
            PathPatternRouter r = (PathPatternRouter) remove;
            pathPatternMap.remove(r.getPathPattern());
        }
    }

    @Override
    public Router get(String id) {
        return routeMap.get(id);
    }

    @Override
    public List<Router> list(Collection<? extends String> ids) {
        List<Router> routers = new ArrayList<>();
        for (String id : ids) {
            Router router = routeMap.get(id);
            if (router == null) {
                continue;
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<Router> all() {
        return Collections.unmodifiableList(new ArrayList<>(routeMap.values()));
    }
}
