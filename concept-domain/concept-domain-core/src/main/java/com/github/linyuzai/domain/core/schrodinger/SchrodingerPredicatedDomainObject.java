package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.AbstractDomainProperties;
import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.linyuzai.domain.core.schrodinger.AbstractSchrodingerDomainObject.list2one;

/**
 * 薛定谔模型代理
 */
@Getter
@Setter
public class SchrodingerPredicatedDomainObject<T extends DomainObject>
        extends AbstractDomainProperties implements DomainObject {

    protected DomainCollection<T> collection;

    protected Predicate<T> predicate;

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
    protected T doGetTarget() {
        List<T> list = this.collection.list()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
        return list2one(getDomainObjectType(), list);
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
        predicate = null;
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
