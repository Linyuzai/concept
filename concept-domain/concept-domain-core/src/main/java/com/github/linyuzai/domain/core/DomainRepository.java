package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.page.Pages;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * 领域存储
 */
public interface DomainRepository<T extends DomainObject, C extends DomainCollection<T>> {

    /**
     * 单个新增
     */
    void create(T object);

    /**
     * 多个新增
     */
    void create(C collection);

    /**
     * 单个更新
     */
    void update(T object);

    /**
     * 多个更新
     */
    void update(C collection);

    /**
     * 单个删除
     */
    void delete(T object);

    /**
     * 多个删除
     */
    void delete(C collection);

    /**
     * 单个 id 查询
     */
    T get(String id);

    /**
     * 多个 id 查询
     */
    C select(Collection<String> ids);

    /**
     * 条件删除
     */
    void delete(Conditions conditions);

    /**
     * 单个条件查询
     */
    T get(Conditions conditions);

    /**
     * 列表条件查询
     */
    C select(Conditions conditions);

    /**
     * 数量条件查询
     */
    Long count(Conditions conditions);

    /**
     * 分页条件查询
     */
    Pages<T> page(Conditions conditions, Pages.Args page);
}
