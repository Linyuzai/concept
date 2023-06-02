package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * 领域工厂
 */
public interface DomainFactory {

    <T extends DomainObject> T createObject(Class<T> cls, String id);

    <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, Predicate<T> predicate);

    <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, String id);

    <C extends DomainCollection<?>> C createCollection(Class<C> cls);

    <C extends DomainCollection<?>> C createCollection(Class<C> cls, Conditions conditions);

    <C extends DomainCollection<?>> C createCollection(Class<C> cls, Collection<String> ids, boolean createObject);

    <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Predicate<T> predicate);

    <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Collection<String> ids);

    <C extends DomainCollection<?>> C createCollection(Class<C> cls, Collection<? extends DomainObject> objects);

    <C extends DomainCollection<?>> C createEmptyCollection(Class<C> cls);
}
