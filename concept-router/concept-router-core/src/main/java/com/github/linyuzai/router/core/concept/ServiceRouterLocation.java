package com.github.linyuzai.router.core.concept;

/**
 * 基于服务的路由位置
 */
public interface ServiceRouterLocation extends Router.Location {

    /**
     * 获得 serviceId
     *
     * @return serviceId
     */
    String getServiceId();

    /**
     * 获得服务主机
     *
     * @return 服务主机
     */
    String getHost();

    /**
     * 获得服务端口
     *
     * @return 服务端口
     */
    int getPort();
}
