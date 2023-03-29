package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainObject;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.Method;

/**
 * 薛定谔模型代理
 */
@Getter
public class ProxySchrodingerDomainObject<T extends DomainObject> extends SchrodingerDomainObject<T> implements DomainObject, DomainProxy {

    protected final Class<T> type;

    public ProxySchrodingerDomainObject(Class<T> type, @NonNull String id, @NonNull DomainContext context) {
        super(id, context);
        this.type = type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass == DomainObject.class || declaringClass == Identifiable.class) {
            return method.invoke(this, args);
        }
        return method.invoke(getTarget(), args);
    }

    @Override
    protected Class<? extends T> getDomainObjectType() {
        return type;
    }
}
