package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerConditionsDomainCollection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerConditionsDomainCollection<T extends DomainObject> extends SchrodingerConditionsDomainCollection<T>
        implements DomainCollection<T>, DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object> {

    protected final Class<? extends DomainCollection<?>> type;

    @Setter
    protected Object extra;

    public ProxySchrodingerConditionsDomainCollection(Class<? extends DomainCollection<?>> type,
                                                      @NonNull DomainContext context,
                                                      @NonNull Conditions conditions) {
        super(context, conditions);
        this.type = type;
    }

    protected Class<? extends DomainObject> getDomainType() {
        return DomainLink.collection(type);
    }
}
