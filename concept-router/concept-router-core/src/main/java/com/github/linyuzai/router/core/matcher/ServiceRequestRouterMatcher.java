package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.RequestRouterSource;
import com.github.linyuzai.router.core.concept.ServiceRequestRouter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ServiceRequestRouterMatcher extends AbstractRouterMatcher<RequestRouterSource, ServiceRequestRouter> {

    @Override
    public ServiceRequestRouter doMatch(RequestRouterSource request, Collection<? extends ServiceRequestRouter> routers) {
        String serviceId = request.getServiceId();
        String path = request.getUri().getPath();
        Map<String, ServiceRequestRouter> map = routers.stream()
                .filter(it -> matchServiceId(serviceId, it))
                .collect(Collectors.toMap(ServiceRequestRouter::getPathPattern, Function.identity()));
        ServiceRequestRouter router = map.get(path);
        if (router != null && router.isEnabled()) {
            return router;
        }
        for (ServiceRequestRouter srr : map.values()) {
            String pathPattern = srr.getPathPattern();
            if (srr.isEnabled() && matchPattern(path, pathPattern)) {
                if (router == null) {
                    router = srr;
                } else {
                    if (comparePattern(path, srr.getPathPattern(), router.getPathPattern())) {
                        router = srr;
                    }
                }
            }
        }
        return router;
    }

    public boolean matchServiceId(String serviceId, ServiceRequestRouter router) {
        return "*".equals(router.getServiceId()) ||
                serviceId.equalsIgnoreCase(router.getServiceId());
    }

    public abstract boolean matchPattern(String path, String pattern);

    public abstract boolean comparePattern(String path, String matchedNew, String matchedBefore);

}
