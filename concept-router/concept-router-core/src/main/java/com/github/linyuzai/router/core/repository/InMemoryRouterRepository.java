package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.exception.RouterException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRouterRepository implements RouterRepository {

    protected final Map<String, Router> routerMap = new ConcurrentHashMap<>();

    protected final Map<String, Router> pathPatternMap = new ConcurrentHashMap<>();

    @Override
    public void add(Collection<? extends Router> routes) {
        update(routes);
    }

    @Override
    public void update(Collection<? extends Router> routes) {
        for (Router router : routes) {
            PathPatternRouter ppr = (PathPatternRouter) router;
            Router exist = pathPatternMap.get(ppr.getPathPattern());
            if (exist != null && !exist.getId().equals(ppr.getId())) {
                throw new RouterException("Path pattern duplicated");
            }
            Router r = routerMap.get(ppr.getId());
            if (r != null) {
                pathPatternMap.remove(((PathPatternRouter) r).getPathPattern());
            }
            routerMap.put(router.getId(), router);
            pathPatternMap.put(ppr.getPathPattern(), ppr);
        }
    }

    @Override
    public void remove(Collection<? extends String> ids) {
        for (String id : ids) {
            Router remove = routerMap.remove(id);
            if (remove == null) {
                continue;
            }
            PathPatternRouter r = (PathPatternRouter) remove;
            pathPatternMap.remove(r.getPathPattern());
        }
    }

    @Override
    public Router get(String id) {
        return routerMap.get(id);
    }

    @Override
    public List<Router> list(Collection<? extends String> ids) {
        List<Router> routers = new ArrayList<>();
        for (String id : ids) {
            Router router = routerMap.get(id);
            if (router == null) {
                continue;
            }
            routers.add(router);
        }
        return sort(routers);
    }

    @Override
    public List<Router> all() {
        return Collections.unmodifiableList(sort(new ArrayList<>(routerMap.values())));
    }

    protected List<Router> sort(List<Router> routers) {
        routers.sort((o1, o2) -> (int) (o2.getTimestamp() - o1.getTimestamp()));
        return routers;
    }
}
