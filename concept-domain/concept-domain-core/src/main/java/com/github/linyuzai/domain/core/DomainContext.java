package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.proxy.ProxyListableDomainCollection;
import com.github.linyuzai.domain.core.proxy.ProxySchrodingerDomainCollection;
import com.github.linyuzai.domain.core.proxy.ProxySchrodingerDomainObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 领域上下文
 */
public interface DomainContext {

    /**
     * 通过类获得实例
     */
    <T> T get(Class<T> type);

    default <T extends DomainObject> T create(Class<T> cls, String id) {
        return new ProxySchrodingerDomainObject(cls, id, this).create(cls);
    }

    default <C extends DomainCollection<?>> C create(Class<C> cls) {
        return new ProxySchrodingerDomainCollection<C>(cls, this).create(cls);
    }

    default <C extends DomainCollection<?>> C create(Class<C> cls, Conditions conditions) {
        return new ProxySchrodingerDomainCollection<C>(cls, this, conditions).create(cls);
    }

    default <C extends DomainCollection<?>> C create(Class<C> cls, Collection<? extends DomainObject> objects) {
        return new ProxyListableDomainCollection<>(new ArrayList<>(objects)).create(cls);
    }
}
