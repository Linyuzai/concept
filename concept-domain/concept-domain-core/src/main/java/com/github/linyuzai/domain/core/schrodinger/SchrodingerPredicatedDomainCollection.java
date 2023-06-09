package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 薛定谔的集合模型
 */
@Getter
@RequiredArgsConstructor
public class SchrodingerPredicatedDomainCollection<T extends DomainObject>
        extends AbstractDomainProperties implements DomainCollection<T> {

    @NonNull
    protected final DomainCollection<T> collection;

    @NonNull
    protected final Predicate<T> predicate;

    protected Map<String, T> target;

    public Map<String, T> getTarget() {
        if (this.target == null) {
            load();
        }
        return this.target;
    }

    protected Collection<T> doGetTarget() {
        return collection.list().stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public T get(String id) {
        return getTarget().get(id);
    }

    @Override
    public boolean contains(String id) {
        return getTarget().containsKey(id);
    }

    @Override
    public List<T> list() {
        return new ArrayList<>(getTarget().values());
    }

    /**
     * 流式数据查询
     */
    @Override
    public Stream<T> stream() {
        return getTarget().values().stream();
    }

    /**
     * 获得数量
     */
    @Override
    public Long count() {
        return Integer.valueOf(getTarget().size()).longValue();
    }

    @Override
    public synchronized void load() {
        if (this.target == null) {
            this.target = doGetTarget().stream()
                    .collect(Collectors.toMap(Identifiable::getId, Function.identity()));
        }
    }

    @Override
    public synchronized void release() {
        target = null;
    }
}
