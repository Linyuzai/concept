package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.AbstractDomainProperties;
import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.Setter;

/**
 * 薛定谔模型代理
 */
@Getter
@Setter
public class SchrodingerLimitedDomainObject<T extends DomainObject>
        extends AbstractDomainProperties implements DomainObject {

    protected DomainCollection<T> collection;

    /**
     * 领域模型 id
     */
    protected String id;

    /**
     * 被代理的领域模型
     */
    protected T target;

    public T getTarget() {
        if (this.target == null) {
            load();
        }
        return this.target;
    }

    /**
     * 获得被代理的对象
     */
    protected T doGetTarget() {
        T domain = collection.get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainObjectType(), id);
        }
        return domain;
    }

    @Override
    public synchronized void load() {
        if (this.target == null) {
            this.target = doGetTarget();
        }
    }

    @Override
    public synchronized void unload() {
        this.target = null;
    }

    @Override
    public void release() {
        collection = null;
        id = null;
        target = null;
        clearProperties();
        onRelease();
    }

    protected void onRelease() {

    }

    protected Class<? extends T> getDomainObjectType() {
        return DomainLink.generic(getClass(), 0);
    }

}
