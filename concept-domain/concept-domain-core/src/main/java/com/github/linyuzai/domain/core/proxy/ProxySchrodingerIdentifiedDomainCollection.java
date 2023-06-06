package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerIdentifiedDomainCollection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerIdentifiedDomainCollection<T extends DomainObject> extends SchrodingerIdentifiedDomainCollection<T>
        implements DomainCollection<T>, DomainProxy, DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<T>, DomainProxy.ExtraAccess<Object> {

    protected final Class<? extends DomainCollection<?>> type;

    protected Conditions conditions;

    @Setter
    protected Object extra;

    public ProxySchrodingerIdentifiedDomainCollection(Class<? extends DomainCollection<?>> type,
                                                      @NonNull DomainContext context,
                                                      @NonNull Collection<String> ids) {
        super(context, ids);
        this.type = type;
    }

    protected Class<? extends DomainObject> getDomainType() {
        return DomainLink.collection(type);
    }

    @Override
    public Conditions getConditions() {
        if (conditions == null) {
            conditions = Conditions.ids(ids);
        }
        return conditions;
    }
}
