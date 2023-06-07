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
        implements DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess<T>,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object>,
        DomainContext.Aware {

    @NonNull
    protected final Class<? extends DomainCollection<?>> type;

    protected DomainContext context;

    public ProxyListableDomainCollection(@NonNull Class<? extends DomainCollection<?>> type,
                                         @NonNull List<T> list) {
        super(list);
        this.type = type;
    }

    public ProxyListableDomainCollection(@NonNull Class<? extends DomainCollection<?>> type,
                                         DomainContext context,
                                         @NonNull List<T> list) {
        super(list);
        this.type = type;
        this.context = context;
    }

    @Override
    public Conditions getConditions() {
        return withPropertyKey(Conditions.class, () -> Conditions.ids(list.stream()
                .map(Identifiable::getId)
                .collect(Collectors.toList())));
    }

    @Override
    public DomainRepository<T, ?> getRepository() {
        return withPropertyValue(DomainRepository.class, () -> {
            if (context == null) {
                return null;
            }
            Class<? extends DomainRepository<T, ?>> clazz =
                    DomainLink.repository(type);
            return context.get(clazz);
        });
    }
}
