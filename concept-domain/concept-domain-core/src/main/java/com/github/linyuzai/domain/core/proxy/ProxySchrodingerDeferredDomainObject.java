package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDeferredDomainObject;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Supplier;

/**
 * 薛定谔模型代理
 */
@Getter
public class ProxySchrodingerDeferredDomainObject<T extends DomainObject>
        extends SchrodingerDeferredDomainObject<T>
        implements DomainObject, DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object> {

    @NonNull
    protected final Class<T> type;

    public ProxySchrodingerDeferredDomainObject(@NonNull Class<T> type,
                                                @NonNull DomainContext context,
                                                @NonNull Supplier<T> supplier) {
        super(context, supplier);
        this.type = type;
    }

    @Override
    public Object getProxied() {
        return getTarget();
    }

    @Override
    protected Class<? extends T> getDomainObjectType() {
        return type;
    }

    @Override
    public Conditions getConditions() {
        return withPropertyKey(Conditions.class, () -> Conditions.id(getId()));
    }
}
