package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerOnceDomainObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * 薛定谔模型代理
 */
@Getter
public class ProxySchrodingerOnceDomainObject<T extends DomainObject> extends SchrodingerOnceDomainObject<T>
        implements DomainObject, DomainProxy, DomainProxy.AccessAdapter<T, Object> {

    protected final Class<T> type;

    @Setter
    protected Object extra;

    public ProxySchrodingerOnceDomainObject(Class<T> type, @NonNull DomainCollection<T> collection, @NonNull Predicate<T> predicate) {
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
