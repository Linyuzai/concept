package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class ProxyDomainFactory implements DomainFactory {

    private DomainContext context;

    public <T extends DomainObject> T create(Class<T> cls, String id) {
        return new ProxySchrodingerDomainObject(cls, id, context).create(cls);
    }

    public <T extends DomainObject> T create(Class<T> cls, String id, DomainCollection<T> collection) {
        return new ProxySchrodingerCollectionDomainObject(id, collection).create(cls);
    }

    public <C extends DomainCollection<?>> C create(Class<C> cls) {
        return new ProxySchrodingerDomainCollection<C>(cls, context).create(cls);
    }

    public <C extends DomainCollection<?>> C create(Class<C> cls, Conditions conditions) {
        return new ProxySchrodingerDomainCollection<C>(cls, context, conditions).create(cls);
    }

    public <C extends DomainCollection<?>> C create(Class<C> cls, Collection<? extends DomainObject> objects) {
        return new ProxyListableDomainCollection<>(new ArrayList<>(objects)).create(cls);
    }
}
