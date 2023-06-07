package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerLimitedDomainObject;
import lombok.Getter;
import lombok.NonNull;

/**
 * 薛定谔模型代理
 */
@Getter
public class ProxySchrodingerLimitedDomainObject<T extends DomainObject>
        extends SchrodingerLimitedDomainObject<T>
        implements DomainObject, DomainProxy, DomainProxy.AccessAdapter<T, Object> {

    @NonNull
    protected final Class<T> type;

    public ProxySchrodingerLimitedDomainObject(@NonNull Class<T> type,
                                               @NonNull DomainCollection<T> collection,
                                               @NonNull String id) {
        super(collection, id);
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
        return withPropertyKey(Conditions.class, () ->
                Conditions.from(AccessAdapter.super.getConditions())
                        .equal(Conditions.KEY_ID, id));
    }
}
