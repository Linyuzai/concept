package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ProxyDomainFactory implements DomainFactory {

    private DomainContext context;

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, String id) {
        return new ProxySchrodingerIdentifiedDomainObject<>(cls, context, id).create(cls);
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, Conditions conditions) {
        return new ProxySchrodingerConditionsDomainObject<>(cls, context, conditions).create(cls);
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, Supplier<T> supplier) {
        return new ProxySchrodingerDeferredDomainObject<>(cls, context, supplier).create(cls);
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, String id) {
        return new ProxySchrodingerLimitedDomainObject<>(cls, collection, id).create(cls);
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, Predicate<T> predicate) {
        return new ProxySchrodingerPredicatedDomainObject<>(cls, collection, predicate).create(cls);
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> Map<String, T> createObject(
            Class<C> cls, Collection<String> ownerIds, Function<Collection<String>,
            Map<String, String>> idMapping) {
        return createObject(DomainLink.collection(cls), cls, ownerIds, idMapping);
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> Map<String, T> createObject(
            Class<T> dCls, Class<C> cCls, Collection<String> ownerIds,
            Function<Collection<String>, Map<String, String>> idMapping) {
        CachedFunction<Collection<String>, Map<String, String>> function =
                new CachedFunction<>(idMapping);
        C collection = createCollection(cCls, () -> {
            Set<String> ids = new HashSet<>(function.apply(ownerIds).values());
            C c = createCollection(cCls, ids);
            return ids.stream()
                    .map(it -> createObject(dCls, c, it))
                    .collect(Collectors.toList());
        });

        Map<String, T> map = new LinkedHashMap<>();
        for (String ownerId : ownerIds) {
            Map<String, String> apply = function.apply(ownerIds);
            String id = apply.get(ownerId);
            if (id == null) {
                continue;
            }
            T object = createObject(dCls, () -> collection.get(id));
            map.put(ownerId, object);
        }

        return map;
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls, Conditions conditions) {
        return new ProxySchrodingerConditionsDomainCollection<C>(cls, context, this, conditions).create(cls);
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, Supplier<Collection<T>> supplier) {
        return new ProxySchrodingerDeferredDomainCollection<>(cls, context, this, supplier).create(cls);
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls, Collection<String> ids) {
        return new ProxySchrodingerIdentifiedDomainCollection<>(cls, context, this, ids).create(cls);
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Collection<String> ids) {
        return new ProxySchrodingerLimitedDomainCollection<>(cls, collection, ids).create(cls);
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Predicate<T> predicate) {
        return new ProxySchrodingerPredicatedDomainCollection<T>(cls, collection, predicate).create(cls);
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> Map<String, C> createCollection(Class<C> cls, Collection<String> ownerIds, Function<Collection<String>, Map<String, ? extends Collection<String>>> idsMapping) {
        return createCollection(DomainLink.collection(cls), cls, ownerIds, idsMapping);
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> Map<String, C> createCollection(Class<T> dCls, Class<C> cCls, Collection<String> ownerIds, Function<Collection<String>, Map<String, ? extends Collection<String>>> idsMapping) {
        CachedFunction<Collection<String>, Map<String, ? extends Collection<String>>> function =
                new CachedFunction<>(idsMapping);
        C collection = createCollection(cCls, () -> {
            Set<String> ids = function.apply(ownerIds)
                    .values()
                    .stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            C c = createCollection(cCls, ids);
            return ids.stream()
                    .map(it -> createObject(dCls, c, it))
                    .collect(Collectors.toList());
        });

        Map<String, C> map = new LinkedHashMap<>();
        for (String ownerId : ownerIds) {
            Map<String, ? extends Collection<String>> apply = function.apply(ownerIds);
            Collection<String> ids = apply.get(ownerId);
            C c = createCollection(cCls, () -> ids.stream()
                    .map(collection::get)
                    .collect(Collectors.toList()));
            map.put(ownerId, c);
        }
        return map;
    }

    @Override
    public <T extends DomainObject> T wrapObject(Class<T> cls, T object) {
        return new ProxyExtendableDomainObject<>(cls, context, object).create(cls);
    }

    @Override
    public <C extends DomainCollection<?>> C wrapCollection(Class<C> cls, Collection<? extends DomainObject> objects) {
        return new ProxyListableDomainCollection<>(cls, context, new ArrayList<>(objects)).create(cls);
    }

    @Override
    public <C extends DomainCollection<?>> C emptyCollection(Class<C> cls) {
        return wrapCollection(cls, Collections.emptyList());
    }

    @RequiredArgsConstructor
    private static class CachedFunction<T, R> implements Function<T, R> {

        private final Function<T, R> function;

        private R result;

        private boolean mark;

        @Override
        public R apply(T t) {
            if (mark) {
                return result;
            }
            result = function.apply(t);
            mark = true;
            return result;
        }
    }
}
