package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.Router;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 路由仓库
 */
public interface RouterRepository {

    /**
     * 初始化
     */
    default void initialize() {

    }

    /**
     * 销毁
     */
    default void destroy() {

    }

    /**
     * 添加路由
     *
     * @param routers 路由
     */
    default void add(Router... routers) {
        add(Arrays.asList(routers));
    }

    /**
     * 添加路由
     *
     * @param routers 路由
     */
    void add(Collection<? extends Router> routers);

    /**
     * 更新路由
     *
     * @param routers 路由
     */
    default void update(Router... routers) {
        update(Arrays.asList(routers));
    }

    /**
     * 更新路由
     *
     * @param routers 路由
     */
    void update(Collection<? extends Router> routers);

    /**
     * 移除路由
     *
     * @param ids 路由ID
     */
    default void remove(String... ids) {
        remove(Arrays.asList(ids));
    }

    /**
     * 移除路由
     *
     * @param ids 路由ID
     */
    void remove(Collection<? extends String> ids);

    /**
     * 通过ID获得路由
     *
     * @param id 路由ID
     * @return 路由
     */
    Router get(String id);

    /**
     * 根据路由ID获得路由列表
     *
     * @param ids 路由ID
     * @return 路由列表
     */
    default List<Router> list(String... ids) {
        return list(Arrays.asList(ids));
    }

    /**
     * 根据路由ID获得路由列表
     *
     * @param ids 路由ID
     * @return 路由列表
     */
    List<Router> list(Collection<? extends String> ids);

    /**
     * 获得所有路由
     *
     * @return 所有路由
     */
    List<Router> all();
}
