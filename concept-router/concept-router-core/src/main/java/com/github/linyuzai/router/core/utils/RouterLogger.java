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

/**
 * 路由日志
 */
@AllArgsConstructor
public class RouterLogger implements RouterEventListener {

    private Consumer<String> info;

    private BiConsumer<String, Throwable> error;

    /**
     * 在路由匹配和定位时打印日志
     *
     * @param event 路由事件
     */
    @Override
    public void onEvent(Object event) {
        if (event instanceof RouterMatchEvent) {
            Router.Source source = ((RouterMatchEvent) event).getSource();
            Router router = ((RouterMatchEvent) event).getRouter();
            if (source instanceof RequestRouterSource) {
                if (router == null) {
                    info(notMatch((RequestRouterSource) source));
                } else {
                    if (router instanceof ServiceRequestRouter) {
                        info(matched((RequestRouterSource) source, (ServiceRequestRouter) router));
                    }
                }
            }
        } else if (event instanceof RouterLocateEvent) {
            Router.Location location = ((RouterLocateEvent) event).getLocation();
            Router router = ((RouterLocateEvent) event).getRouter();
            if (router instanceof ServiceRequestRouter) {
                if (location == Router.Location.UNMATCHED) {
                    info(notLocate((ServiceRequestRouter) router));
                } else if (location == Router.Location.UNAVAILABLE) {
                    info(notAvailable((ServiceRequestRouter) router));
                } else {
                    if (location instanceof ServiceRouterLocation) {
                        info(located((ServiceRouterLocation) location, (ServiceRequestRouter) router));
                    }
                }
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

    public String notAvailable(ServiceRequestRouter router) {
        return "Service unavailable for router '" + getRouter(router) + "'";
    }

    public String located(ServiceRouterLocation location, ServiceRequestRouter router) {
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
