package com.github.linyuzai.router.core.concept;

import java.util.Collection;
import java.util.List;

/**
 * 路由概念
 */
public interface RouterConcept {

    /**
     * 路由
     *
     * @param source    被路由的来源
     * @param locations 所有可路由的位置
     * @return 需要路由到的位置
     */
    Router.Location route(Router.Source source, Collection<? extends Router.Location> locations);

    /**
     * 获得所有的路由
     *
     * @return 所有的路由
     */
    List<Router> routers();

    /**
     * 添加一个路由
     *
     * @param router 路由
     */
    void add(Router router);

    /**
     * 修改一个路由
     *
     * @param router 路由
     */
    void update(Router router);

    /**
     * 删除一个路由
     *
     * @param id 路由ID
     */
    void delete(String id);

    /**
     * 发布一个路由事件
     *
     * @param event 路由事件
     */
    void publish(Object event);
}
