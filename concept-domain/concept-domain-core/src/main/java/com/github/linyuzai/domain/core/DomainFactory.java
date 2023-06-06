package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 领域工厂
 */
public interface DomainFactory {

    <T extends DomainObject> T createObject(Class<T> cls, String id);

    <T extends DomainObject> T createObject(Class<T> cls, Conditions conditions);

    <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, String id);

    <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, Predicate<T> predicate);

    <T extends DomainObject, C extends DomainCollection<T>> Map<String, T> createObject(Class<T> dCls, Class<C> cCls, Collection<String> limitedIds, Map<String, String> idMapping);

    <C extends DomainCollection<?>> C createCollection(Class<C> cls, Collection<String> ids);

    <C extends DomainCollection<?>> C createCollection(Class<C> cls, Conditions conditions);

    <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Collection<String> ids);

    <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Predicate<T> predicate);

    <T extends DomainObject, C extends DomainCollection<T>> Map<String, C> createCollection(Class<T> dCls, Class<C> cCls, Collection<String> limitedIds, Map<String, Collection<String>> idsMapping);

    <C extends DomainCollection<?>> C wrapCollection(Class<C> cls, Collection<? extends DomainObject> objects);

    <C extends DomainCollection<?>> C emptyCollection(Class<C> cls);
}
