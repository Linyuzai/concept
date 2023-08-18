package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerConditionsDomainCollection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.function.Function;

/**
 * 薛定谔的集合模型
 */
@Getter
@Setter
public class ProxySchrodingerConditionsDomainCollection<T extends DomainObject>
        extends SchrodingerConditionsDomainCollection<T>
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
