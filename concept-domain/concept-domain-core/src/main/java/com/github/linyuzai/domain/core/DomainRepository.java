package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.page.Pages;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * 领域存储
 */
public interface DomainRepository<T extends DomainObject> {

    /**
     * 单个新增
     */
    void create(T object);

    /**
     * 多个新增
     */
    void create(Collection<? extends T> objects);

    /**
     * 单个更新
     */
    void update(T object);

    /**
     * 多个更新
     */
    void update(Collection<? extends T> objects);

    /**
     * 单个删除
     */
    void delete(T object);

    /**
     * 根据 id 单个删除
     */
    void delete(String id);

    /**
     * 多个删除
     */
    void delete(Collection<String> ids);

    /**
     * 单个 id 查询
     */
    T get(String id);

    /**
     * 多个 id 查询
     */
    Collection<T> select(Collection<String> ids);

    /**
     * 条件删除
     */
    void delete(Conditions conditions);

    /**
     * 单个条件查询
     */
    T query(Conditions conditions);

    /**
     * 数量条件查询
     */
    Long count(Conditions conditions);

    /**
     * 列表条件查询
     */
    List<T> list(Conditions conditions);

    /**
     * 分页条件查询
     */
    Pages<T> page(Conditions conditions, Pages.Args page);

    /**
     * 流式读取
     */
    Stream<T> stream(Conditions conditions);
}
