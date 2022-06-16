package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.PathPatternRoute;
import com.github.linyuzai.router.core.concept.Route;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractRouteMatcher implements RouteMatcher {
    @Override
    public Route match(Route.Source source, Collection<? extends Route> routes) {
        return doMatch(getServiceId(source), getPath(source), routes);
    }

    public Route doMatch(String serviceId, String path, Collection<? extends Route> routes) {
        Map<String, PathPatternRoute> map = routes.stream()
                .map(PathPatternRoute.class::cast)
                .filter(it -> matchServiceId(serviceId, it))
                .collect(Collectors.toMap(PathPatternRoute::getPathPattern, Function.identity()));
        PathPatternRoute route = map.get(path);
        if (route != null && route.isEnabled()) {
            return route;
        }
        int matchedLength = -1;
        for (Map.Entry<String, PathPatternRoute> entry : map.entrySet()) {
            String key = entry.getKey();
            PathPatternRoute value = entry.getValue();
            if (value.isEnabled() && matchPattern(key, path)) {
                String keyReplaced = key.replaceAll("\\*", "")
                        .replaceAll("\\?", "")
                        .replaceAll("/", "");
                int keyLength = keyReplaced.length();
                if (keyLength > matchedLength) {
                    matchedLength = keyLength;
                    route = value;
                }
            }
        }
        return route;
    }

    public abstract String getServiceId(Route.Source source);

    public abstract String getPath(Route.Source source);

    public abstract boolean matchPattern(String pattern, String path);

    public boolean matchServiceId(String serviceId, PathPatternRoute router) {
        return "*".equals(router.getServiceId()) ||
                "**".equals(router.getServiceId()) ||
                serviceId.equals(router.getServiceId());
    }
}
