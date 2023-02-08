package com.github.linyuzai.domain.core;

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
     * 获得所属者
     */
    Object getOwner();

    /**
     * 根据 id 查询领域模型
     */
    T get(String id);

    /**
     * 流式查询
     */
    Stream<? extends T> stream();

    /**
     * 数量
     */
    Long count();
}
