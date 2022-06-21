package com.github.linyuzai.router.core.utils;

import com.github.linyuzai.router.core.concept.RequestRouterSource;
import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.concept.ServiceRequestRouter;
import com.github.linyuzai.router.core.concept.ServiceRouterLocation;
import com.github.linyuzai.router.core.event.RouterEventListener;
import com.github.linyuzai.router.core.event.RouterLocateEvent;
import com.github.linyuzai.router.core.event.RouterMatchEvent;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@AllArgsConstructor
public class RouterLogger implements RouterEventListener {

    private Consumer<String> info;

    private BiConsumer<String, Throwable> error;

    @Override
    public void onEvent(Object event) {
        if (event instanceof RouterMatchEvent) {
            Router.Source source = ((RouterMatchEvent) event).getSource();
            Router router = ((RouterMatchEvent) event).getRouter();
            if (router == null) {
                info(notMatch((RequestRouterSource) source));
            } else {
                if (source instanceof RequestRouterSource && router instanceof ServiceRequestRouter) {
                    info(matched((RequestRouterSource) source, (ServiceRequestRouter) router));
                }
            }
        } else if (event instanceof RouterLocateEvent) {
            Router.Location location = ((RouterLocateEvent) event).getLocation();
            Router router = ((RouterLocateEvent) event).getRouter();
            if (location == null) {
                info(notLocate((ServiceRequestRouter) router));
            }
            if (location instanceof ServiceRouterLocation && router instanceof ServiceRequestRouter) {
                info(located((ServiceRouterLocation) location, (ServiceRequestRouter) router));
            }
        }
    }

    public String notMatch(RequestRouterSource source) {
        return "No router matched for url path '" + source.getUri().getPath() + "'";
    }

    public String matched(RequestRouterSource source, ServiceRequestRouter router) {
        return "Url path '" + getSource(source) + "' match router '" + getRouter(router) + "'";
    }

    public String notLocate(ServiceRequestRouter router) {
        return "No service located for router '" + getRouter(router) + "'";
    }

    public String located(ServiceRouterLocation location, ServiceRequestRouter router) {
        if (location.getHost() == null) {
            return "Service unavailable for router '" + getRouter(router) + "'";
        }
        return "Router '" + getRouter(router) + "' locate service '" + getLocation(location) + "'";
    }

    public String getSource(RequestRouterSource source) {
        return source.getUri().getPath();
    }

    public String getLocation(ServiceRouterLocation location) {
        return location.getServiceId() + "@" + location.getHost() + ":" + location.getPort();
    }

    public String getRouter(ServiceRequestRouter router) {
        StringBuilder builder = new StringBuilder();
        if (!"*".equals(router.getServiceId())) {
            builder.append(router.getServiceId()).append("@");
        }
        builder.append(router.getHost());
        if (!"*".equals(router.getPort())) {
            builder.append(":").append(router.getPort());
        }
        return router.getPathPattern() + " => " + builder;
    }

    public String appendTag(String msg) {
        return "Router >> " + msg;
    }

    public void info(String msg) {
        info.accept(appendTag(msg));
    }

    public void error(String msg, Throwable e) {
        error.accept(appendTag(msg), e);
    }
}
