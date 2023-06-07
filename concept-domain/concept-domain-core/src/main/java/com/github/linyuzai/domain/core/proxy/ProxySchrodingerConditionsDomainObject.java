package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerConditionsDomainObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 薛定谔模型代理
 */
@Getter
public class ProxySchrodingerConditionsDomainObject<T extends DomainObject> extends SchrodingerConditionsDomainObject<T>
        implements DomainObject, DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object> {

    @NonNull
    protected final Class<T> type;

    @Setter
    protected Object extra;

    public ProxySchrodingerConditionsDomainObject(@NonNull Class<T> type,
                                                  @NonNull DomainContext context,
                                                  @NonNull Conditions conditions) {
        super(context, conditions);
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
}
