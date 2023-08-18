package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ProxyExtendableDomainObject<T extends DomainObject> extends AbstractDomainProperties
        implements DomainObject, DomainProxy,
        DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object>,
        DomainContext.Aware {

    @NonNull
    protected Class<? extends DomainObject> type;

    protected T object;

    protected DomainContext context;

    @Override
    public String getId() {
        return object.getId();
    }

    @Override
    public Object getProxied() {
        return object;
    }

    @Override
    public Conditions getConditions() {
        return withPropertyKey(Conditions.class, () -> Conditions.id(object.getId()));
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

    @Override
    public void release() {
        object = null;
        clearProperties();
        onRelease();
    }

    protected void onRelease() {

    }
}
