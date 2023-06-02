package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public class ProxyDomainFactory implements DomainFactory {

    private DomainContext context;

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, String id) {
        return new ProxySchrodingerDomainObject<>(cls, id, context).create(cls);
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, Predicate<T> predicate) {
        return new ProxySchrodingerOnceDomainObject<>(cls, collection, predicate).create(cls);
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, DomainCollection<T> collection, String id) {
        return createObject(cls, collection, t -> t.getId().equals(id));
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls) {
        return new ProxySchrodingerDomainCollection<C>(cls, context).create(cls);
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls, Conditions conditions) {
        return new ProxySchrodingerDomainCollection<C>(cls, context, conditions).create(cls);
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls, Collection<String> ids, boolean createObject) {
        if (createObject) {
            List<DomainObject> objects = new ArrayList<>();
            for (String id : ids) {
                DomainObject object = createObject(DomainLink.collection(cls), id);
                objects.add(object);
            }
            return createCollection(cls, objects);
        } else {
            return createCollection(cls, Conditions.ids(ids));
        }
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Predicate<T> predicate) {
        return new ProxySchrodingerOnceDomainCollection<T>(cls, collection, predicate).create(cls);
    }

    @Override
    public <T extends DomainObject, C extends DomainCollection<T>> C createCollection(Class<C> cls, C collection, Collection<String> ids) {
        Set<String> set = new HashSet<>(ids);
        return createCollection(cls, collection, t -> set.contains(t.getId()));
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls, Collection<? extends DomainObject> objects) {
        return new ProxyListableDomainCollection<>(cls, context, new ArrayList<>(objects)).create(cls);
    }

    @Override
    public <C extends DomainCollection<?>> C createEmptyCollection(Class<C> cls) {
        return createCollection(cls, Collections.emptyList());
    }
}
