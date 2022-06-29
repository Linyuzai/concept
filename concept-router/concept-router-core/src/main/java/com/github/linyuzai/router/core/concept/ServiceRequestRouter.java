package com.github.linyuzai.router.core.concept;

/**
 * 基于服务请求的路由
 */
public interface ServiceRequestRouter extends Router {

    /**
     * 获得 serviceId
     *
     * @return serviceId
     */
    String getServiceId();

    /**
     * 获得路径匹配模式
     *
     * @return 路径匹配模式
     */
    String getPathPattern();

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
    String getPort();
}
