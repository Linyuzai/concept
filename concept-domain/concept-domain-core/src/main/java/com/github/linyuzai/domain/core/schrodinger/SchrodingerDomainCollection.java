package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.AbstractDomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.exception.DomainIdRequiredException;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 薛定谔的集合模型
 *
 * @param <T>
 */
@Getter
public abstract class SchrodingerDomainCollection<T extends DomainObject> extends AbstractDomainCollection<T> {

    /**
     * 所属 id
     */
    protected String ownerId;

    /**
     * 领域上下文
     */
    protected DomainContext context;

    /**
     * 获得所属者
     */
    @Override
    public Object getOwner() {
        if (owner == null) {
            this.owner = doGetOwner();
        }
        return owner;
    }

    /**
     * 获得所属者
     */
    protected Object doGetOwner() {
        DomainRepository<?> repository = context.get(getOwnerRepositoryType());
        Object owner = repository.get(ownerId);
        if (owner == null) {
            throw new DomainNotFoundException(getOwnerType(), ownerId);
        }
        return owner;
    }

    /**
     * 根据 id 获得领域模型
     */
    @Override
    public T get(String id) {
        T exist = objects.get(id);
        if (exist == null) {
            T get = doGet(id);
            if (get != null) {
                objects.put(get.getId(), get);
            }
        }
        return exist;
    }

    /**
     * 根据 id 获得领域模型
     */
    protected T doGet(String id) {
        if (id == null) {
            throw new DomainIdRequiredException(getDomainType());
        }
        DomainRepository<? extends T> repository = context.get(getDomainRepositoryType());
        T domain = repository.get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainType(), id);
        }
        return domain;
    }

    /**
     * 流式数据查询
     */
    @Override
    public Stream<? extends T> stream() {
        DomainRepository<? extends T> repository = context.get(getDomainRepositoryType());
        return repository.stream(obtainConditions());
    }

    /**
     * 获得沸点的评论数
     */
    @Override
    public Long count() {
        DomainRepository<? extends T> repository = context.get(getDomainRepositoryType());
        return repository.count(obtainConditions());
    }

    /**
     * 生成条件
     */
    protected Conditions obtainConditions() {
        return onConditionsObtain(new Conditions(), ownerId);
    }

    /**
     * 条件生成回调
     */
    protected abstract Conditions onConditionsObtain(Conditions conditions, String id);

    /**
     * 领域模型类
     */
    protected abstract Class<? extends T> getDomainType();

    /**
     * 领域模型存储
     */
    protected abstract Class<? extends DomainRepository<? extends T>> getDomainRepositoryType();

    /**
     * 所属者类
     */
    protected abstract Class<?> getOwnerType();

    /**
     * 所属者模型存储
     */
    protected abstract Class<? extends DomainRepository<?>> getOwnerRepositoryType();
}
