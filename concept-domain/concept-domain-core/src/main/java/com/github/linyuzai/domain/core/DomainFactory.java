package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;

import java.util.Collection;

/**
 * 领域工厂
 */
public interface DomainFactory {

    <T extends DomainObject> T create(Class<T> cls, String id);

    <T extends DomainObject> T create(Class<T> cls, String id, DomainCollection<T> collection);

    <C extends DomainCollection<?>> C create(Class<C> cls);

    <C extends DomainCollection<?>> C create(Class<C> cls, Conditions conditions);

    <C extends DomainCollection<?>> C create(Class<C> cls, Collection<? extends DomainObject> objects);
}
