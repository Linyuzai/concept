package com.github.linyuzai.router.core.locator;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 基于泛型的路由定位器
 *
 * @param <R> 路由类型
 * @param <L> 位置类型
 */
public abstract class AbstractRouterLocator<R extends Router, L extends Router.Location> implements RouterLocator {

    @SuppressWarnings("unchecked")
    @Override
    public Router.Location locate(Router router, Collection<? extends Router.Location> locations) {
        return doLocate((R) router, locations.stream().map(it -> (L) it).collect(Collectors.toList()));
    }

    /**
     * 路由定位
     *
     * @param router    路由
     * @param locations 位置
     * @return 定位到的位置
     */
    public abstract Router.Location doLocate(R router, Collection<? extends L> locations);
}
