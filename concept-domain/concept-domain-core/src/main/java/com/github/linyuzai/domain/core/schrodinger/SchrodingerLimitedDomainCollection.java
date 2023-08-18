package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.AbstractDomainProperties;
import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 薛定谔的集合模型
 */
@Getter
@Setter
public class SchrodingerLimitedDomainCollection<T extends DomainObject>
        extends AbstractDomainProperties implements DomainCollection<T> {

    protected DomainCollection<T> collection;

    protected Collection<String> ids;

    /**
     * 被代理的领域模型
     */
    protected Map<String, T> target;

    public Map<String, T> getTarget() {
        if (this.target == null) {
            load();
        }
        return this.target;
    }

    protected Collection<T> doGetTarget() {
        return collection.list().stream().filter(it -> ids.contains(it.getId())).collect(Collectors.toList());
    }

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public T get(String id) {
        if (!ids.contains(id)) {
            return null;
        }
        return getTarget().get(id);
    }

    @Override
    public boolean contains(String id) {
        return ids.contains(id);
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
    public synchronized void unload() {
        this.target = null;
    }

    @Override
    public void release() {
        collection = null;
        ids = null;
        target = null;
        clearProperties();
        onRelease();
    }

    protected void onRelease() {

    }
}
