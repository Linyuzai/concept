package com.github.linyuzai.connection.loadbalance.core.repository;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * 连接仓库
 */
public interface ConnectionRepository {

    /**
     * 获得一个连接
     *
     * @param id   连接 id
     * @param type 连接类型
     * @return 连接或 null
     */
    Connection get(Object id, String type);

    /**
     * 通过连接类型获得对应的连接集合
     *
     * @param type 连接类型
     * @return 对应的连接集合
     */
    Collection<Connection> select(String type);

    /**
     * 获得所有连接
     *
     * @return 所有连接
     */
    Collection<Connection> all();

    /**
     * 添加连接
     *
     * @param connection 连接
     */
    void add(Connection connection);

    /**
     * 移除连接
     *
     * @param connection 被移除的连接
     * @return 连接仓库中存在则返回对应的连接，否则返回 null
     */
    Connection remove(Connection connection);

    /**
     * 移除连接
     *
     * @param id   连接 id
     * @param type 连接类型
     * @return 连接仓库中存在则返回对应的连接，否则返回 null
     */
    Connection remove(Object id, String type);

    /**
     * 获得连接流
     *
     * @return 连接流
     */
    Stream<Connection> stream();

    /**
     * 通过连接类型获得对应的连接流
     *
     * @param type 连接类型
     * @return 对应的连接流
     */
    Stream<Connection> stream(String type);
}
