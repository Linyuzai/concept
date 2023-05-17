package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProxyListableDomainCollection<T extends DomainObject> extends ListableDomainCollection<T>
        implements DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object>,
        DomainContext.Aware {

    protected Class<? extends DomainCollection<?>> type;

    protected DomainContext context;

    protected Conditions conditions;

    protected DomainRepository<T, ?> repository;

    protected Object extra;

    public ProxyListableDomainCollection(Class<? extends DomainCollection<?>> type, @NonNull List<T> list) {
        super(list);
        this.type = type;
    }

    public ProxyListableDomainCollection(Class<? extends DomainCollection<?>> type, DomainContext context, @NonNull List<T> list) {
        super(list);
        this.type = type;
        this.context = context;
    }

    @Override
    public void setContext(DomainContext context) {
        this.context = context;
    }

    @Override
    public Conditions getConditions() {
        if (conditions == null) {
            conditions = Conditions.ids(list.stream()
                    .map(Identifiable::getId)
                    .collect(Collectors.toList()));
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
                    DomainLink.repository(DomainLink.collection(type));
            repository = context.get(rType);
        }
        return repository;
    }
}
