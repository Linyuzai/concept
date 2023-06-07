package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerLimitedDomainCollection;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerLimitedDomainCollection<T extends DomainObject>
        extends SchrodingerLimitedDomainCollection<T>
        implements DomainCollection<T>, DomainProxy, DomainProxy.AccessAdapter<T, Object> {

    @NonNull
    protected final Class<? extends DomainCollection<?>> type;

    public ProxySchrodingerLimitedDomainCollection(@NonNull Class<? extends DomainCollection<?>> type,
                                                   @NonNull DomainCollection<T> collection,
                                                   @NonNull Collection<String> ids) {
        super(collection, ids);
        this.type = type;
    }

    @Override
    public Conditions getConditions() {
        return withPropertyKey(Conditions.class, () ->
                Conditions.from(AccessAdapter.super.getConditions())
                        .in(Conditions.KEY_ID, ids));
    }
}
