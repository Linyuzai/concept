package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractRouterMatcher implements RouterMatcher {
    @Override
    public Router match(Router.Source source, Collection<? extends Router> routes) {
        return doMatch(getServiceId(source), getPath(source), routes);
    }

    public Router doMatch(String serviceId, String path, Collection<? extends Router> routes) {
        Map<String, PathPatternRouter> map = routes.stream()
                .map(PathPatternRouter.class::cast)
                .filter(it -> matchServiceId(serviceId, it))
                .collect(Collectors.toMap(PathPatternRouter::getPathPattern, Function.identity()));
        PathPatternRouter router = map.get(path);
        if (router != null && router.isEnabled()) {
            return router;
        }
        int matchedLength = -1;
        for (Map.Entry<String, PathPatternRouter> entry : map.entrySet()) {
            String key = entry.getKey();
            PathPatternRouter value = entry.getValue();
            if (value.isEnabled() && matchPattern(key, path)) {
                String keyReplaced = key.replaceAll("\\*", "")
                        .replaceAll("\\?", "")
                        .replaceAll("/", "");
                int keyLength = keyReplaced.length();
                if (keyLength > matchedLength) {
                    matchedLength = keyLength;
                    router = value;
                }
            }
        }
        return router;
    }

    public abstract String getServiceId(Router.Source source);

    public abstract String getPath(Router.Source source);

    public abstract boolean matchPattern(String pattern, String path);

    public boolean matchServiceId(String serviceId, PathPatternRouter router) {
        return "*".equals(router.getServiceId()) ||
                serviceId.equalsIgnoreCase(router.getServiceId());
    }
}
