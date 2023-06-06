package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * 薛定谔的集合模型
 */
@Getter
@RequiredArgsConstructor
public class SchrodingerLimitedDomainCollection<T extends DomainObject> implements DomainCollection<T> {

    @NonNull
    protected final DomainCollection<T> collection;

    @NonNull
    protected final Collection<String> ids;

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public T get(String id) {
        load();
        return collection.get(id);
    }

    @Override
    public boolean contains(String id) {
        load();
        return collection.contains(id);
    }


    @Override
    public List<T> list() {
        load();
        return collection.list();
    }

    /**
     * 流式数据查询
     */
    @Override
    public Stream<T> stream() {
        load();
        return collection.stream();
    }

    /**
     * 获得数量
     */
    @Override
    public Long count() {
        load();
        return collection.count();
    }


    @Override
    public void load() {
        collection.load();
    }

    @Override
    public void release() {
        collection.release();
    }
}
