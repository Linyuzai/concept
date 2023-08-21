package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.recycler.DomainRecycler;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Getter
@RequiredArgsConstructor
public class ProxyDomainFactory implements DomainFactory {

    private final DomainContext context;

    private final DomainRecycler recycler;

    protected <T extends DomainObject, P extends DomainProxy> T obtain(
            Class<P> proxyType, Supplier<P> supplier, Class<T> cls,
            Consumer<P> onCreate, Consumer<P> onInit) {
        T reuseOrCreate = recycler.reuse(proxyType, cls, () -> {
            P creator = supplier.get();
            onCreate.accept(creator);
            return creator.create(cls);
        });
        P creator = (P) Proxy.getInvocationHandler(reuseOrCreate);
        onInit.accept(creator);
        return reuseOrCreate;
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, @NonNull String id) {
        return obtain(ProxySchrodingerIdentifiedDomainObject.class,
                ProxySchrodingerIdentifiedDomainObject::new, cls,
                create -> {
                    create.setContext(context);
                    create.setType(cls);
                }, init -> {
                    init.setId(id);
                });
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, @NonNull Conditions conditions) {
        return obtain(ProxySchrodingerConditionsDomainObject.class,
                ProxySchrodingerConditionsDomainObject::new, cls,
                create -> {
                    create.setContext(context);
                    create.setType(cls);
                }, init -> {
                    init.setConditions(conditions);
                });
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, @NonNull Supplier<T> supplier) {
        return obtain(ProxySchrodingerDeferredDomainObject.class,
                ProxySchrodingerDeferredDomainObject::new, cls,
                create -> {
                    create.setContext(context);
                    create.setType(cls);
                }, init -> {
                    init.setSupplier(supplier);
                });
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls,
                                                   @NonNull DomainCollection<T> collection,
                                                   @NonNull String id) {
        return obtain(ProxySchrodingerLimitedDomainObject.class,
                ProxySchrodingerLimitedDomainObject::new, cls,
                create -> {
                    create.setType(cls);
                }, init -> {
                    init.setCollection(collection);
                    init.setId(id);
                });
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, Predicate<T> predicate) {
        return obtain(ProxySchrodingerPredicatedDomainObject.class,
                ProxySchrodingerPredicatedDomainObject::new, cls,
                create -> {
                    create.setType(cls);
                }, init -> {
                    init.setCollection(collection);
                    init.setPredicate(predicate);
                });
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
            List<T> list = ids.stream()
                    .map(it -> createObject(dCls, c, it))
                    .collect(Collectors.toList());
            return wrapCollection(cCls, list);
        });

        Map<String, T> map = new LinkedHashMap<>();
        for (String ownerId : ownerIds) {
            T object = createObject(dCls, () -> {
                Map<String, String> apply = function.apply(ownerIds);
                String id = apply.get(ownerId);
                return collection.get(id);
            });
            map.put(ownerId, object);
        }

        return map;
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls, Conditions conditions) {
        return obtain(ProxySchrodingerConditionsDomainCollection.class,
                ProxySchrodingerConditionsDomainCollection::new, cls,
                create -> {
                    create.setContext(context);
                    create.setFactory(this);
                    create.setType(cls);
                }, init -> {
                    init.setConditions(conditions);
                });
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, Supplier<C> supplier) {
        return obtain(ProxySchrodingerDeferredDomainCollection.class,
                ProxySchrodingerDeferredDomainCollection::new, cls,
                create -> {
                    create.setContext(context);
                    create.setFactory(this);
                    create.setType(cls);
                }, init -> {
                    init.setSupplier(supplier);
                });
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls, Collection<String> ids) {
        return obtain(ProxySchrodingerIdentifiedDomainCollection.class,
                ProxySchrodingerIdentifiedDomainCollection::new, cls,
                create -> {
                    create.setContext(context);
                    create.setFactory(this);
                    create.setType(cls);
                }, init -> {
                    init.setIds(ids);
                });
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Collection<String> ids) {
        return obtain(ProxySchrodingerLimitedDomainCollection.class,
                ProxySchrodingerLimitedDomainCollection::new, cls,
                create -> {
                    create.setType(cls);
                }, init -> {
                    init.setCollection(collection);
                    init.setIds(ids);
                });
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Predicate<T> predicate) {
        return obtain(ProxySchrodingerPredicatedDomainCollection.class,
                ProxySchrodingerPredicatedDomainCollection::new, cls,
                create -> {
                    create.setType(cls);
                }, init -> {
                    init.setCollection(collection);
                    init.setPredicate(predicate);
                });
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
            List<T> list = ids.stream()
                    .map(it -> createObject(dCls, c, it))
                    .collect(Collectors.toList());
            return wrapCollection(cCls, list);
        });

        Map<String, C> map = new LinkedHashMap<>();
        for (String ownerId : ownerIds) {
            C c = createCollection(cCls, () -> {
                Map<String, ? extends Collection<String>> apply = function.apply(ownerIds);
                Collection<String> ids = apply.get(ownerId);
                List<T> list = ids.stream()
                        .map(collection::get)
                        .collect(Collectors.toList());
                return wrapCollection(cCls, list);
            });
            map.put(ownerId, c);
        }
        return map;
    }

    @Override
    public <T extends DomainObject> T wrapObject(Class<T> cls, T object) {
        return obtain(ProxyExtendableDomainObject.class,
                ProxyExtendableDomainObject::new, cls,
                create -> {
                    create.setContext(context);
                    create.setType(cls);
                }, init -> {
                    init.setObject(object);
                });
    }

    @Override
    public <C extends DomainCollection<?>> C wrapCollection(Class<C> cls, Collection<? extends DomainObject> objects) {
        return obtain(ProxyListableDomainCollection.class,
                ProxyListableDomainCollection::new, cls,
                create -> {
                    create.setContext(context);
                    create.setType(cls);
                }, init -> {
                    init.setList(new ArrayList<>(objects));
                });
    }

    @Override
    public <C extends DomainCollection<?>> C emptyCollection(Class<C> cls) {
        return wrapCollection(cls, Collections.emptyList());
    }

    @RequiredArgsConstructor
    private static class CachedFunction<T, R> implements Function<T, R> {

        private final Function<T, R> function;

        private R result;

        private volatile boolean first = true;

        @Override
        public R apply(T t) {
            if (first) {
                synchronized (this) {
                    if (first) {
                        result = function.apply(t);
                        first = false;
                    }
                }
            }
            return result;
        }
    }
}
