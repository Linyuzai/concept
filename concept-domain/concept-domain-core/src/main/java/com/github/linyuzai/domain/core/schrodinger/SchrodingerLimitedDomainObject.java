package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.AbstractDomainProperties;
import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
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
public class SchrodingerLimitedDomainObject<T extends DomainObject>
        extends AbstractDomainProperties implements DomainObject {

    @NonNull
    protected final DomainCollection<T> collection;

    /**
     * 领域模型 id
     */
    @NonNull
    protected final String id;

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
        T domain = collection.get(id);
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

    protected Class<? extends T> getDomainObjectType() {
        return DomainLink.generic(getClass(), 0);
    }

}
