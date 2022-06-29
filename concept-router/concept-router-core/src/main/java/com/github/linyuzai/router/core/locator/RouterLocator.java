package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;

/**
 * 路由定位器
 */
public interface RouterLocator {

    /**
     * 路由定位
     *
     * @param router    路由
     * @param locations 位置
     * @return 定位到的位置
     */
    Router.Location locate(Router router, Collection<? extends Router.Location> locations);
}
