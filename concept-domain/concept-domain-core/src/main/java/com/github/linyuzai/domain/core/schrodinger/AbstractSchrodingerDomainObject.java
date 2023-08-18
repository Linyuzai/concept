package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.AbstractDomainProperties;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.exception.DomainMultipleFoundException;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * 薛定谔模型代理
 */
@Getter
public abstract class AbstractSchrodingerDomainObject<T extends DomainObject>
        extends AbstractDomainProperties implements DomainObject {

    @Setter
    @NonNull
    protected DomainContext context;

    protected DomainRepository<T, ?> repository;

    /**
     * 被代理的领域模型
     */
    protected T target;

    @Override
    public String getId() {
        return getTarget().getId();
    }

    public T getTarget() {
        if (this.target == null) {
            load();
        }
        return this.target;
    }

    /**
     * 获得被代理的对象
     */
    protected abstract T doGetTarget();

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
        target = null;
        clearProperties();
        onRelease();
    }

    protected void onRelease() {

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

    public static <E> E list2one(Class<? extends E> type, List<E> list) {
        int size = list.size();
        switch (size) {
            case 0:
                throw new DomainNotFoundException(type);
            case 1:
                return list.get(0);
            default:
                throw new DomainMultipleFoundException(type, size);
        }
    }
}
