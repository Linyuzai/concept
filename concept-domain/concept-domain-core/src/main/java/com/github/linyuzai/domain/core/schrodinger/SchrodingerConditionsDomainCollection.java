package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.exception.DomainIdRequiredException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 薛定谔的集合模型
 */
@Getter
@RequiredArgsConstructor
public class SchrodingerConditionsDomainCollection<T extends DomainObject> implements DomainCollection<T> {

    @NonNull
    protected final DomainContext context;

    @NonNull
    protected final Conditions conditions;

    protected DomainRepository<T, ?> repository;

    protected Map<String, T> targetMap;

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public T get(String id) {
        if (id == null) {
            throw new DomainIdRequiredException(getDomainType());
        }
        load();
        return targetMap.get(id);
    }

    @Override
    public boolean contains(String id) {
        if (id == null) {
            throw new DomainIdRequiredException(getDomainType());
        }
        load();
        return targetMap.containsKey(id);
    }

    @Override
    public List<T> list() {
        load();
        return new ArrayList<>(targetMap.values());
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
        load();
        return targetMap.values().stream();
    }

    /**
     * 获得数量
     */
    @Override
    public Long count() {
        load();
        return Integer.valueOf(targetMap.size()).longValue();
    }

    @Override
    public void load() {
        if (this.targetMap == null) {
            this.targetMap = getRepository().select(getConditions())
                    .list()
                    .stream()
                    .collect(Collectors.toMap(Identifiable::getId, Function.identity()));
        }
    }

    @Override
    public void release() {
        targetMap = null;
    }

    /**
     * 领域模型类
     */
    protected Class<? extends DomainObject> getDomainType() {
        return DomainLink.generic(getClass(), 0);
    }

    /**
     * 领域模型存储
     */
    protected Class<? extends DomainRepository<T, ?>> getDomainRepositoryType() {
        return DomainLink.repository(getDomainType());
    }
}
