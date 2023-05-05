package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainCollection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerDomainCollection<T extends DomainObject> extends SchrodingerDomainCollection<T>
        implements DomainCollection<T>, DomainProxy, DomainProxy.ContextAccess,
        DomainProxy.ConditionsAccess, DomainProxy.RepositoryAccess<T>,
        DomainProxy.CollectionAccess<T>, DomainProxy.ExtraAccess<Object> {

    protected final Class<? extends DomainCollection<?>> type;

    @Setter
    protected Object extra;

    public ProxySchrodingerDomainCollection(Class<? extends DomainCollection<?>> type, @NonNull DomainContext context) {
        super(context);
        this.type = type;
    }

    public ProxySchrodingerDomainCollection(Class<? extends DomainCollection<?>> type, @NonNull DomainContext context, Conditions conditions) {
        super(context, conditions);
        this.type = type;
    }

    protected Class<? extends DomainObject> getDomainType() {
        return DomainLink.collection(type);
    }

    @Override
    public DomainCollection<T> getCollection() {
        return this;
    }
}
