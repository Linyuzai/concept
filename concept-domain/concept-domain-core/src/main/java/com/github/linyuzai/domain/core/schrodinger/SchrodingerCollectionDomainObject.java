package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 薛定谔模型代理
 */
@Getter
@RequiredArgsConstructor
public class SchrodingerCollectionDomainObject<T extends DomainObject> implements DomainObject {

    /**
     * 领域模型 id
     */
    @NonNull
    protected final String id;

    @NonNull
    protected final DomainCollection<? extends T> collection;

    /**
     * 被代理的领域模型
     */
    protected T target;

    public T getTarget() {
        if (this.target == null) {
            this.target = doGetTarget();
        }
        return this.target;
    }

    /**
     * 获得被代理的对象
     */
    public T doGetTarget() {
        T domain = this.collection.get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainObjectType(), id);
        }
        return domain;
    }

    @Override
    public void load() {
        this.collection.load();
        this.target = doGetTarget();
    }

    @Override
    public void release() {
        this.target = null;
        this.collection.release();
    }

    protected Class<? extends T> getDomainObjectType() {
        return DomainLink.generic(getClass(), 0);
    }
}
