package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.stream.Collectors;

@Getter
@Setter
public class ProxyListableDomainCollection<T extends DomainObject> extends ListableDomainCollection<T>
        implements DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object>,
        DomainContext.Aware {

    @NonNull
    protected Class<? extends DomainCollection<?>> type;

    protected DomainContext context;

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
                    DomainLink.repository(DomainLink.collection(type));
            return context.get(clazz);
        });
    }
}
