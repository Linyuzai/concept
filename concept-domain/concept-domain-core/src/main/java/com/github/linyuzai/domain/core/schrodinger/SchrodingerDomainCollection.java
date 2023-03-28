package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.exception.DomainIdRequiredException;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
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
public class SchrodingerDomainCollection<T extends DomainObject> implements DomainCollection<T> {

    /**
     * 领域上下文
     */
    @NonNull
    protected final DomainContext context;

    protected Conditions conditions = Conditions.EMPTY;

    protected Map<String, T> targetMap;

    protected List<T> targetList;

    protected Long targetCount;

    public SchrodingerDomainCollection(@NonNull DomainContext context, Conditions conditions) {
        this.context = context;
        this.conditions = conditions;
    }

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public T get(String id) {
        if (id == null) {
            throw new DomainIdRequiredException(getDomainType());
        }
        if (targetMap == null) {
            targetMap = new HashMap<>();
        }
        T exist = targetMap.get(id);
        if (exist == null) {
            T domain = doGet(id);
            if (domain == null) {
                throw new DomainNotFoundException(getDomainType(), id);
            }
            targetMap.put(id, domain);

            updateTargetList(id);

            return domain;
        } else {
            return exist;
        }
    }

    protected T doGet(String id) {
        DomainRepository<T, ?> repository = context.get(getDomainRepositoryType());
        return repository.get(Conditions.from(conditions).equal("id", id));
    }

    @Override
    public List<T> list() {
        if (targetList == null) {

            targetList = doList();

            updateTargetMap();

            updateTargetCount();
        }
        return targetList;
    }

    protected List<T> doList() {
        DomainRepository<T, ?> repository = context.get(getDomainRepositoryType());
        return repository.select(getConditions()).list();
    }

    /**
     * 流式数据查询
     */
    @Override
    public Stream<T> stream() {
        if (targetList == null) {
            List<T> list = new ArrayList<>();
            return doStream()
                    .peek(list::add)
                    .onClose(() -> updateTargetList(list));
        } else {
            return targetList.stream();
        }
    }

    protected Stream<T> doStream() {
        DomainRepository<T, ?> repository = context.get(getDomainRepositoryType());
        return repository.select(getConditions()).stream();
    }

    /**
     * 获得数量
     */
    @Override
    public Long count() {
        if (targetCount == null) {
            DomainRepository<T, ?> repository = context.get(getDomainRepositoryType());
            targetCount = repository.count(getConditions());
        }
        return targetCount;
    }

    protected void updateTargetList(String id) {
        if (targetList == null) {
            return;
        }
        Set<String> ids = targetList.stream()
                .map(Identifiable::getId)
                .collect(Collectors.toSet());
        if (!ids.contains(id)) {
            targetList = null;
        }
    }

    protected void updateTargetList(List<T> list) {
        targetList = list;
    }

    protected void updateTargetMap() {
        targetMap = targetList.stream()
                .collect(Collectors.toMap(Identifiable::getId, Function.identity()));
    }

    protected void updateTargetCount() {
        targetCount = Integer.valueOf(targetList.size()).longValue();
    }

    @Override
    public void load() {
        list();
    }

    @Override
    public void release() {
        targetMap.clear();
        targetList = null;
        targetCount = null;
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
