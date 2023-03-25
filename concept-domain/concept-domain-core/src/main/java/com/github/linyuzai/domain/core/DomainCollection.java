package com.github.linyuzai.domain.core;

import java.util.List;
import java.util.stream.Stream;

/**
 * 领域模型集合
 *
 * @param <T>
 */
public interface DomainCollection<T extends DomainObject> extends DomainObject {

    @Override
    default String getId() {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据 id 查询领域模型
     */
    T get(String id);

    /**
     * 列表查询
     */
    List<T> list();

    /**
     * 流式查询
     */
    Stream<T> stream();

    /**
     * 数量
     */
    Long count();

    default void refresh() {
        refresh(false);
    }

    default void refresh(boolean force) {

    }
}
