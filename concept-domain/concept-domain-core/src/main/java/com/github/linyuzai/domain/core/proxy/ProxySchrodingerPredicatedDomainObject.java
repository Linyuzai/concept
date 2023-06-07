package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerPredicatedDomainObject;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Predicate;

/**
 * 薛定谔模型代理
 */
@Getter
public class ProxySchrodingerPredicatedDomainObject<T extends DomainObject> extends SchrodingerPredicatedDomainObject<T>
        implements DomainObject, DomainProxy, DomainProxy.AccessAdapter<T, Object> {

    @NonNull
    protected final Class<T> type;

    public ProxySchrodingerPredicatedDomainObject(@NonNull Class<T> type,
                                                  @NonNull DomainCollection<T> collection,
                                                  @NonNull Predicate<T> predicate) {
        super(collection, predicate);
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
