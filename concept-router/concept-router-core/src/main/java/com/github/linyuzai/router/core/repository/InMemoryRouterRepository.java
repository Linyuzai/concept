package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.exception.RouterException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的路由仓库实现
 */
public class InMemoryRouterRepository implements RouterRepository {

    /**
     * 路由缓存
     */
    protected final Map<String, Router> routerMap = new ConcurrentHashMap<>();

    /**
     * 路径匹配模式缓存，用于判断路径匹配模式是否重复
     */
    protected final Map<String, Router> pathPatternMap = new ConcurrentHashMap<>();

    @Override
    public void add(Collection<? extends Router> routes) {
        update(routes);
    }

    @Override
    public void update(Collection<? extends Router> routes) {
        for (Router router : routes) {
            PathPatternRouter ppr = (PathPatternRouter) router;
            //是否存在路径匹配模式相同并且ID不同的路由
            //如果存在则抛出异常
            Router exist = pathPatternMap.get(ppr.getPathPattern());
            if (exist != null && !exist.getId().equals(ppr.getId())) {
                throw new RouterException("Path pattern duplicated");
            }
            //如果原来存在对应ID的路由则先把之前的路径匹配模式移除
            //不移除就可能导致之前的路径匹配模式的旧数据触发上面重复的异常的逻辑
            Router r = routerMap.get(ppr.getId());
            if (r != null) {
                pathPatternMap.remove(((PathPatternRouter) r).getPathPattern());
            }
            //更新两个缓存
            routerMap.put(router.getId(), router);
            pathPatternMap.put(ppr.getPathPattern(), ppr);
        }
    }

    @Override
    public void remove(Collection<? extends String> ids) {
        for (String id : ids) {
            Router remove = routerMap.remove(id);
            if (remove == null) {
                continue;
            }
            //两个缓存同时删除
            PathPatternRouter r = (PathPatternRouter) remove;
            pathPatternMap.remove(r.getPathPattern());
        }
    }

    @Override
    public Router get(String id) {
        return routerMap.get(id);
    }

    @Override
    public List<Router> list(Collection<? extends String> ids) {
        List<Router> routers = new ArrayList<>();
        for (String id : ids) {
            Router router = routerMap.get(id);
            if (router == null) {
                continue;
            }
            routers.add(router);
        }
        return sort(routers);
    }

    @Override
    public List<Router> all() {
        return Collections.unmodifiableList(sort(new ArrayList<>(routerMap.values())));
    }

    /**
     * 通过时间戳来排序，越晚的排在越前面
     *
     * @param routers 需要排序的路由
     * @return 排序之后的路由
     */
    protected List<Router> sort(List<Router> routers) {
        routers.sort((o1, o2) -> (int) (o2.getTimestamp() - o1.getTimestamp()));
        return routers;
    }
}
