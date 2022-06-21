package com.github.linyuzai.router.core.concept;

public interface ServiceRequestRouter extends Router {

    String getServiceId();

    String getPathPattern();

    String getHost();

    String getPort();
}
