package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.proxy.ListableDomainCollection;
import com.github.linyuzai.domain.core.proxy.ProxyDomainCollection;
import com.github.linyuzai.domain.core.proxy.ProxyDomainObject;

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
        return new ProxyDomainObject(cls, id, this).create(cls);
    }

    default <C extends DomainCollection<?>> C create(Class<C> cls) {
        return new ProxyDomainCollection<C>(cls, this).create(cls);
    }

    default <C extends DomainCollection<?>> C create(Class<C> cls, Conditions conditions) {
        return new ProxyDomainCollection<C>(cls, this, conditions).create(cls);
    }

    default <C extends DomainCollection<?>> C create(Class<C> cls, Collection<? extends DomainObject> objects) {
        return new ListableDomainCollection<>(new ArrayList<>(objects)).create(cls);
    }
}
