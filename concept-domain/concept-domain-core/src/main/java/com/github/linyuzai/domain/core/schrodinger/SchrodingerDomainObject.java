package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.*;

/**
 * 薛定谔模型代理
 */
@Getter
@RequiredArgsConstructor
public class SchrodingerDomainObject<T extends DomainObject> implements DomainObject {

    /**
     * 领域模型 id
     */
    @NonNull
    protected final String id;

    @NonNull
    protected final DomainContext context;

    protected DomainRepository<T, ?> repository;

    /**
     * 被代理的领域模型
     */
    protected T target;

    public T getTarget() {
        load();
        return this.target;
    }

    /**
     * 获得被代理的对象
     */
    public T doGetTarget() {
        T domain = getRepository().get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainObjectType(), id);
        }
        return domain;
    }

    @Override
    public void load() {
        if (this.target == null) {
            this.target = doGetTarget();
        }
    }

    @Override
    public void release() {
        this.target = null;
    }

    public DomainRepository<T, ?> getRepository() {
        if (repository == null) {
            repository = context.get(getDomainRepositoryType());
        }
        return repository;
    }

    protected Class<? extends T> getDomainObjectType() {
        return DomainLink.generic(getClass(), 0);
    }

    /**
     * 被代理的领域模型的存储
     */
    protected Class<? extends DomainRepository<T, ?>> getDomainRepositoryType() {
        return DomainLink.repository(getDomainObjectType());
    }
}
