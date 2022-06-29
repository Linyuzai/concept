package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;

/**
 * 路由匹配器
 */
public interface RouterMatcher {

    /**
     * 匹配路由
     *
     * @param source  路由来源
     * @param routers 路由
     * @return 匹配到的路由
     */
    Router match(Router.Source source, Collection<? extends Router> routers);
}
