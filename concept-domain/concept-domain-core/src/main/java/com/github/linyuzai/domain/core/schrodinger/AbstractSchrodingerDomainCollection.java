package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.exception.DomainIdRequiredException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 薛定谔的集合模型
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractSchrodingerDomainCollection<T extends DomainObject> implements DomainCollection<T> {

    @NonNull
    protected final DomainContext context;

    protected DomainRepository<T, ?> repository;

    protected Map<String, T> target;

    public Map<String, T> getTarget() {
        load();
        return this.target;
    }

    public abstract List<T> doGetTarget();

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public T get(String id) {
        if (id == null) {
            throw new DomainIdRequiredException(getDomainObjectType());
        }
        return getTarget().get(id);
    }

    @Override
    public boolean contains(String id) {
        if (id == null) {
            throw new DomainIdRequiredException(getDomainObjectType());
        }
        return getTarget().containsKey(id);
    }

    @Override
    public List<T> list() {
        return new ArrayList<>(getTarget().values());
    }

    public DomainRepository<T, ?> getRepository() {
        if (repository == null) {
            repository = context.get(getDomainRepositoryType());
        }
        return repository;
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

    protected Function<T, T> mapping() {
        return Function.identity();
    }

    @Override
    public void load() {
        if (this.target == null) {
            this.target = doGetTarget().stream()
                    .collect(Collectors.toMap(Identifiable::getId, mapping()));
        }
    }

    @Override
    public void release() {
        target = null;
    }

    /**
     * 领域模型类
     */
    protected Class<T> getDomainObjectType() {
        return DomainLink.generic(getClass(), 0);
    }

    /**
     * 领域模型存储
     */
    protected Class<? extends DomainRepository<T, ?>> getDomainRepositoryType() {
        return DomainLink.repository(getDomainObjectType());
    }
}
