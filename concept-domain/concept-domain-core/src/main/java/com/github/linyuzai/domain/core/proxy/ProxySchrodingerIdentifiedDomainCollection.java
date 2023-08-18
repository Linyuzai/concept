package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerIdentifiedDomainCollection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.function.Function;

/**
 * 薛定谔的集合模型
 */
@Getter
@Setter
public class ProxySchrodingerIdentifiedDomainCollection<T extends DomainObject>
        extends SchrodingerIdentifiedDomainCollection<T>
        implements DomainCollection<T>, Function<T, T>,
        DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object> {

    @NonNull
    protected Class<? extends DomainCollection<?>> type;

    @NonNull
    protected DomainFactory factory;

    @Override
    protected Class<T> getDomainObjectType() {
        return DomainLink.collection(type);
    }

    @Override
    public Conditions getConditions() {
        return withPropertyKey(Conditions.class, () -> Conditions.ids(ids));
    }

    @Override
    protected Function<T, T> mapping() {
        if (DomainProxy.hasAccess(getDomainObjectType())) {
            return this;
        }
        return super.mapping();
    }

    @Override
    public T apply(T t) {
        return factory.wrapObject(getDomainObjectType(), t);
    }
}
