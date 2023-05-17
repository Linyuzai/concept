package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 薛定谔模型代理
 */
@Getter
public class ProxySchrodingerDomainObject<T extends DomainObject> extends SchrodingerDomainObject<T>
        implements DomainObject, DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object> {

    protected final Class<T> type;

    protected Conditions conditions;

    @Setter
    protected Object extra;

    public ProxySchrodingerDomainObject(Class<T> type, @NonNull String id, @NonNull DomainContext context) {
        super(id, context);
        this.type = type;
    }

    @Override
    public Object getProxied() {
        return getTarget();
    }

    @Override
    protected Class<? extends T> getDomainObjectType() {
        return type;
    }

    @Override
    public Conditions getConditions() {
        if (conditions == null) {
            conditions = Conditions.id(id);
        }
        return conditions;
    }
}
