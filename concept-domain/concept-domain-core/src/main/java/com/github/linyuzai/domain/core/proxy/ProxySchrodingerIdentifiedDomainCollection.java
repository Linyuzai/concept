package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerIdentifiedDomainCollection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;
import java.util.function.Function;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerIdentifiedDomainCollection<T extends DomainObject>
        extends SchrodingerIdentifiedDomainCollection<T>
        implements DomainCollection<T>, Function<T, T>,
        DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object> {

    @NonNull
    protected final Class<? extends DomainCollection<?>> type;

    @NonNull
    protected final DomainFactory factory;

    protected Conditions conditions;

    @Setter
    protected Object extra;

    public ProxySchrodingerIdentifiedDomainCollection(@NonNull Class<? extends DomainCollection<?>> type,
                                                      @NonNull DomainContext context,
                                                      @NonNull DomainFactory factory,
                                                      @NonNull Collection<String> ids) {
        super(context, ids);
        this.type = type;
        this.factory = factory;
    }

    @Override
    protected Class<T> getDomainObjectType() {
        return DomainLink.collection(type);
    }

    @Override
    public Conditions getConditions() {
        if (conditions == null) {
            conditions = Conditions.ids(ids);
        }
        return conditions;
    }

    @Override
    protected Function<T, T> mapping() {
        if (DomainProxy.hasAccessOrAnnotation(getDomainObjectType())) {
            return this;
        }
        return super.mapping();
    }

    @Override
    public T apply(T t) {
        return factory.wrapObject(getDomainObjectType(), t);
    }
}
