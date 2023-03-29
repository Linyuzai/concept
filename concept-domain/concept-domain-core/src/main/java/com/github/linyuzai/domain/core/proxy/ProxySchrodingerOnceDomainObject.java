package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerOnceDomainObject;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * 薛定谔模型代理
 */
public class ProxySchrodingerOnceDomainObject<T extends DomainObject> extends SchrodingerOnceDomainObject<T>
        implements DomainObject, DomainProxy {

    protected final Class<T> type;

    public ProxySchrodingerOnceDomainObject(Class<T> type, @NonNull DomainCollection<T> collection, @NonNull Predicate<T> predicate) {
        super(collection, predicate);
        this.type = type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == DomainObject.class) {
            return method.invoke(this, args);
        }
        return method.invoke(getTarget(), args);
    }

    @Override
    protected Class<? extends T> getDomainObjectType() {
        return type;
    }
}
