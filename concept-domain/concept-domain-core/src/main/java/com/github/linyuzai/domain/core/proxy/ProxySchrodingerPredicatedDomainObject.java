package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerPredicatedDomainObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 薛定谔模型代理
 */
@Getter
@Setter
public class ProxySchrodingerPredicatedDomainObject<T extends DomainObject>
        extends SchrodingerPredicatedDomainObject<T>
        implements DomainObject, DomainProxy, DomainProxy.AccessAdapter<T, Object> {

    @NonNull
    protected Class<T> type;

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
                        .equal(Conditions.ID, getId()));
    }
}
