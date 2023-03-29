package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 薛定谔的集合模型
 */
@Getter
@RequiredArgsConstructor
public class SchrodingerOnceDomainCollection<T extends DomainObject> implements DomainCollection<T> {

    @NonNull
    protected final DomainCollection<T> collection;

    @NonNull
    protected final Predicate<T> predicate;

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public T get(String id) {
        load();
        T domain = collection.get(id);
        if (predicate.test(domain)) {
            return domain;
        }
        throw new DomainNotFoundException(getDomainType(), id);
    }


    @Override
    public List<T> list() {
        load();
        return collection.list().stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 流式数据查询
     */
    @Override
    public Stream<T> stream() {
        load();
        return collection.stream().filter(predicate);
    }

    /**
     * 获得数量
     */
    @Override
    public Long count() {
        load();
        return collection.list().stream().filter(predicate).count();
    }


    @Override
    public void load() {
        collection.load();
    }

    @Override
    public void release() {
        collection.release();
    }

    /**
     * 领域模型类
     */
    protected Class<? extends T> getDomainType() {
        return DomainLink.generic(getClass(), 0);
    }
}
