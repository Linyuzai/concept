package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.exception.DomainIdRequiredException;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.proxy.DomainCollectionProxy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

/**
 * 薛定谔的集合模型
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SchrodingerDomainCollection implements DomainCollection<DomainObject>, DomainCollectionProxy {

    protected final Class<? extends DomainCollection<?>> collectionType;
    /**
     * 领域上下文
     */
    @NonNull
    protected final DomainContext context;

    protected Conditions conditions = Conditions.EMPTY;

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public DomainObject get(String id) {
        if (id == null) {
            throw new DomainIdRequiredException(getDomainType());
        }
        DomainRepository<?, ?> repository = context.get(getDomainRepositoryType());
        DomainObject domain = repository.get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainType(), id);
        }
        return domain;
    }

    @Override
    public List<DomainObject> list() {
        DomainRepository<DomainObject, ?> repository = context.get(getDomainRepositoryType());
        return repository.select(getConditions()).list();
    }

    /**
     * 流式数据查询
     */
    @Override
    public Stream<DomainObject> stream() {
        DomainRepository<DomainObject, ?> repository = context.get(getDomainRepositoryType());
        return repository.select(getConditions()).stream();
    }

    /**
     * 获得沸点的评论数
     */
    @Override
    public Long count() {
        DomainRepository<DomainObject, ?> repository = context.get(getDomainRepositoryType());
        return repository.count(getConditions());
    }

    /**
     * 领域模型类
     */
    protected Class<? extends DomainObject> getDomainType() {
        return DomainLink.collection(collectionType);
    }

    /**
     * 领域模型存储
     */
    protected Class<? extends DomainRepository<DomainObject, ?>> getDomainRepositoryType() {
        return DomainLink.repository(getDomainType());
    }

    @Override
    public Object getInvokeCollection() {
        return this;
    }
}
