package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.schrodinger.SchrodingerDomainCollection;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.Method;

/**
 * 薛定谔的集合模型
 */
@Getter
public class ProxySchrodingerDomainCollection<T extends DomainObject> extends SchrodingerDomainCollection<T>
        implements DomainCollection<T>, DomainProxy {

    protected final Class<? extends DomainCollection<?>> type;

    public ProxySchrodingerDomainCollection(Class<? extends DomainCollection<?>> type, @NonNull DomainContext context) {
        super(context);
        this.type = type;
    }

    public ProxySchrodingerDomainCollection(Class<? extends DomainCollection<?>> type, @NonNull DomainContext context, Conditions conditions) {
        super(context, conditions);
        this.type = type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this, args);
    }

    protected Class<? extends DomainObject> getDomainType() {
        return DomainLink.collection(type);
    }
}
