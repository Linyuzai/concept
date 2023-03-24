package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainCollection;

import java.util.ArrayList;
import java.util.Collection;
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

    static <C extends DomainCollection<?>> C wrap(Class<C> cls, Collection<? extends DomainObject> collection) {
        return new ListableDomainCollection<>(new ArrayList<>(collection)).create(cls);
    }

    static <C extends DomainCollection<?>> C schrodinger(Class<C> cls, DomainContext context) {
        return new SchrodingerDomainCollection(cls, context).create(cls);
    }

    static <C extends DomainCollection<?>> C schrodinger(Class<C> cls, DomainContext context, Conditions conditions) {
        return new SchrodingerDomainCollection(cls, context, conditions).create(cls);
    }
}
