package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 领域工厂
 */
public interface DomainFactory {

    /**
     * 创建指定 id 的领域对象
     */
    <T extends DomainObject> T createObject(Class<T> cls, String id);

    /**
     * 创建指定条件的领域对象
     */
    <T extends DomainObject> T createObject(Class<T> cls, Conditions conditions);

    /**
     * 创建延迟调用的领域对象
     */
    <T extends DomainObject> T createObject(Class<T> cls, Supplier<T> supplier);

    /**
     * 创建在指定集合中指定 id 的领域对象
     */
    <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, String id);

    /**
     * 创建在指定集合断言过滤的领域对象
     */
    <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, Predicate<T> predicate);

    /**
     * 创建 id 关联的领域对象 Map
     */
    <T extends DomainObject, C extends DomainCollection<T>> Map<String, T> createObject(Class<C> cls, Collection<String> ownerIds, Function<Collection<String>, Map<String, String>> idMapping);

    /**
     * 创建 id 关联的领域对象 Map
     */
    <T extends DomainObject, C extends DomainCollection<T>> Map<String, T> createObject(Class<T> dCls, Class<C> cCls, Collection<String> ownerIds, Function<Collection<String>, Map<String, String>> idMapping);

    /**
     * 创建指定 ids 的领域集合
     */
    <C extends DomainCollection<?>> C createCollection(Class<C> cls, Collection<String> ids);

    /**
     * 创建指定条件的领域集合
     */
    <C extends DomainCollection<?>> C createCollection(Class<C> cls, Conditions conditions);

    /**
     * 创建延迟调用的领域集合
     */
    <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, Supplier<Collection<T>> supplier);

    /**
     * 创建在指定集合中指定 ids 的领域集合
     */
    <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Collection<String> ids);

    /**
     * 创建在指定集合断言过滤的领域集合
     */
    <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Predicate<T> predicate);

    /**
     * 创建 id 关联的领域集合 Map
     */
    <T extends DomainObject, C extends DomainCollection<T>> Map<String, C> createCollection(Class<C> cls, Collection<String> ownerIds, Function<Collection<String>, Map<String, ? extends Collection<String>>> idsMapping);

    /**
     * 创建 id 关联的领域集合 Map
     */
    <T extends DomainObject, C extends DomainCollection<T>> Map<String, C> createCollection(Class<T> dCls, Class<C> cCls, Collection<String> ownerIds, Function<Collection<String>, Map<String, ? extends Collection<String>>> idsMapping);

    /**
     * 包装成领域对象
     */
    <T extends DomainObject> T wrapObject(Class<T> cls, T object);

    /**
     * 包装成领域集合
     */
    <C extends DomainCollection<?>> C wrapCollection(Class<C> cls, Collection<? extends DomainObject> objects);

    /**
     * 空集合
     */
    <C extends DomainCollection<?>> C emptyCollection(Class<C> cls);
}
