package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerCollectionDomainObject;
import lombok.NonNull;

import java.lang.reflect.Method;

/**
 * 薛定谔模型代理
 */
public class ProxySchrodingerCollectionDomainObject extends SchrodingerCollectionDomainObject<DomainObject>
        implements DomainObject, DomainProxy {

    public ProxySchrodingerCollectionDomainObject(@NonNull String id, @NonNull DomainCollection<? extends DomainObject> collection) {
        super(id, collection);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果是 getId 则直接返回
        if ("getId".equals(method.getName())) {
            return id;
        }
        if (method.getDeclaringClass() == DomainObject.class) {
            return method.invoke(this, args);
        }
        return method.invoke(getTarget(), args);
    }
}
