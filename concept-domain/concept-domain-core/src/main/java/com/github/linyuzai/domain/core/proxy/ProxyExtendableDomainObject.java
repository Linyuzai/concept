package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ProxyExtendableDomainObject<T extends DomainObject> implements DomainObject, DomainProxy,
        DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object>,
        DomainContext.Aware {

    @NonNull
    protected final T object;

    @NonNull
    protected final Class<? extends DomainObject> type;

    protected DomainContext context;

    protected Conditions conditions;

    protected DomainRepository<T, ?> repository;

    protected Object extra;

    public ProxyExtendableDomainObject(@NonNull Class<? extends DomainObject> type,
                                       DomainContext context,
                                       @NonNull T object) {
        this.type = type;
        this.context = context;
        this.object = object;
    }

    @Override
    public String getId() {
        return object.getId();
    }

    @Override
    public Conditions getConditions() {
        if (conditions == null) {
            conditions = Conditions.id(object.getId());
        }
        return conditions;
    }

    @Override
    public DomainRepository<T, ?> getRepository() {
        if (repository == null) {
            if (context == null) {
                return null;
            }
            Class<? extends DomainRepository<T, ?>> rType =
                    DomainLink.repository(type);
            repository = context.get(rType);
        }
        return repository;
    }
}
