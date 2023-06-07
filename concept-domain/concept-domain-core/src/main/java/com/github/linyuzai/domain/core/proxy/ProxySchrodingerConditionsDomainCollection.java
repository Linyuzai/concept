package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerConditionsDomainCollection;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Function;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerConditionsDomainCollection<T extends DomainObject>
        extends SchrodingerConditionsDomainCollection<T>
        implements DomainCollection<T>, Function<T, T>,
        DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess<T>,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object> {

    @NonNull
    protected final Class<? extends DomainCollection<?>> type;

    @NonNull
    protected final DomainFactory factory;

    public ProxySchrodingerConditionsDomainCollection(@NonNull Class<? extends DomainCollection<?>> type,
                                                      @NonNull DomainContext context,
                                                      @NonNull DomainFactory factory,
                                                      @NonNull Conditions conditions) {
        super(context, conditions);
        this.type = type;
        this.factory = factory;
    }

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
