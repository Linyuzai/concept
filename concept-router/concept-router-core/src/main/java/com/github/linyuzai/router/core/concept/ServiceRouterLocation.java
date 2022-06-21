package com.github.linyuzai.router.core.concept;

public interface ServiceRouterLocation extends Router.Location {

    String getServiceId();

    String getHost();

    int getPort();
}
