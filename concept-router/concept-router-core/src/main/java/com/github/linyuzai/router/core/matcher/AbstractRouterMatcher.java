package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 基于泛型的路由匹配器
 *
 * @param <S> 路由来源类型
 * @param <R> 路由类型
 */
public abstract class AbstractRouterMatcher<S extends Router.Source, R extends Router> implements RouterMatcher {

    @SuppressWarnings("unchecked")
    @Override
    public Router match(Router.Source source, Collection<? extends Router> routers) {
        return doMatch((S) source, routers.stream().map(it -> (R) it).collect(Collectors.toList()));
    }

    /**
     * 匹配路由
     *
     * @param source  路由来源
     * @param routers 路由
     * @return 匹配到的路由
     */
    public abstract Router doMatch(S source, Collection<? extends R> routers);
}
