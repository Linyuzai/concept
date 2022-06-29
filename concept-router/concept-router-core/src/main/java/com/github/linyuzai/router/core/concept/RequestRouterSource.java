package com.github.linyuzai.router.core.concept;

import java.net.URI;

/**
 * 基于请求的路由来源
 */
public interface RequestRouterSource extends Router.Source {

    /**
     * 获得 serviceId
     *
     * @return serviceId
     */
    String getServiceId();

    /**
     * 获得 uri
     *
     * @return uri
     */
    URI getUri();
}
