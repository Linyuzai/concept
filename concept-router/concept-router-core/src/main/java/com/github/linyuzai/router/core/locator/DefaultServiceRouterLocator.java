package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;

/**
 * 基于服务请求的路由定位器的默认实现
 */
public class DefaultServiceRouterLocator extends ServiceRequestRouterLocator {

    /**
     * 如果服务不可用返回未匹配
     *
     * @return 未匹配
     */
    @Override
    public Router.Location getLocationIfUnavailable() {
        return Router.Location.UNMATCHED;
    }
}
