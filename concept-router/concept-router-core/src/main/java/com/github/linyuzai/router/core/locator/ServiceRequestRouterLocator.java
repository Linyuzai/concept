package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.concept.ServiceRequestRouter;
import com.github.linyuzai.router.core.concept.ServiceRouterLocation;

import java.util.Collection;


/**
 * 基于服务请求的路由定位器
 */
public abstract class ServiceRequestRouterLocator extends AbstractRouterLocator<ServiceRequestRouter, ServiceRouterLocation> {

    @Override
    public Router.Location doLocate(ServiceRequestRouter router, Collection<? extends ServiceRouterLocation> locations) {
        for (ServiceRouterLocation location : locations) {
            //有匹配上的直接返回
            if (matchServiceId(location, router) &&
                    matchHost(location, router) &&
                    matchPost(location, router)) {
                return location;
            }
        }
        //没有匹配上的走强制路由
        if (router.isForced()) {
            return Router.Location.UNAVAILABLE;
        }
        //返回不可用时的位置
        return getLocationIfUnavailable();
    }

    /**
     * 等于 '*' 或是全等（忽略大小写）
     *
     * @param location 服务位置
     * @param router   路由
     * @return 服务是否匹配
     */
    public boolean matchServiceId(ServiceRouterLocation location, ServiceRequestRouter router) {
        return "*".equals(router.getServiceId()) ||
                location.getServiceId().equalsIgnoreCase(router.getServiceId());
    }

    /**
     * 全等匹配
     *
     * @param location 服务位置
     * @param router   路由
     * @return 主机是否匹配
     */
    public boolean matchHost(ServiceRouterLocation location, ServiceRequestRouter router) {
        return location.getHost().equals(router.getHost());
    }

    /**
     * 等于 '*' 或是相等
     *
     * @param location 服务位置
     * @param router   路由
     * @return 端口是否匹配
     */
    public boolean matchPost(ServiceRouterLocation location, ServiceRequestRouter router) {
        return "*".equals(router.getPort()) ||
                location.getPort() == Integer.parseInt(router.getPort());
    }

    /**
     * 当服务不可用并且不强制路由时所返回的服务位置
     *
     * @return 服务位置
     */
    public abstract Router.Location getLocationIfUnavailable();
}
