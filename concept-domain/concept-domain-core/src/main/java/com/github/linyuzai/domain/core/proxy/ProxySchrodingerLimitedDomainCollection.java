package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerLimitedDomainCollection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 薛定谔的集合模型
 */
@Getter
@Setter
public class ProxySchrodingerLimitedDomainCollection<T extends DomainObject>
        extends SchrodingerLimitedDomainCollection<T>
        implements DomainCollection<T>, DomainProxy, DomainProxy.AccessAdapter<T, Object> {

    @NonNull
    protected Class<? extends DomainCollection<?>> type;

    @Override
    public Conditions getConditions() {
        return withPropertyKey(Conditions.class, () ->
                Conditions.from(AccessAdapter.super.getConditions())
                        .in(Conditions.ID, ids));
    }
}
