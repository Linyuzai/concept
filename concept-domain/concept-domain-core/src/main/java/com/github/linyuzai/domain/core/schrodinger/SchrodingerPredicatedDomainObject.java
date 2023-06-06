package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.exception.DomainMultipleFoundException;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 薛定谔模型代理
 */
@Getter
@RequiredArgsConstructor
public class SchrodingerPredicatedDomainObject<T extends DomainObject> implements DomainObject {

    @NonNull
    protected final DomainCollection<T> collection;

    @NonNull
    protected final Predicate<T> predicate;

    /**
     * 被代理的领域模型
     */
    protected T target;

    @Override
    public String getId() {
        return getTarget().getId();
    }

    public T getTarget() {
        load();
        return this.target;
    }

    /**
     * 获得被代理的对象
     */
    public T doGetTarget() {
        List<? extends T> list = this.collection.list()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
        int size = list.size();
        switch (size) {
            case 0:
                throw new DomainNotFoundException(getDomainObjectType());
            case 1:
                return list.get(0);
            default:
                throw new DomainMultipleFoundException(getDomainObjectType(), size);
        }
    }

    @Override
    public void load() {
        this.collection.load();
        if (this.target == null) {
            this.target = doGetTarget();
        }
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
