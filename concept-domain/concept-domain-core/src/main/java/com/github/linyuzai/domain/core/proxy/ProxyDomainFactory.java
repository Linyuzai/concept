package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.condition.Conditions;
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
        return new ProxySchrodingerIdentifiedDomainObject<>(cls, context, id).create(cls);
    }

    @Override
    public <T extends DomainObject> T createObject(Class<T> cls, Conditions conditions) {
        return new ProxySchrodingerConditionsDomainObject<>(cls, context, conditions).create(cls);
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
    public <T extends DomainObject, C extends DomainCollection<T>> Map<String, T> createObject(Class<T> dCls, Class<C> cCls, Collection<String> limitedIds, Map<String, String> idMapping) {
        C collection = createCollection(cCls, limitedIds);
        Map<String, T> map = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : idMapping.entrySet()) {
            T object = createObject(dCls, collection, entry.getValue());
            map.put(entry.getKey(), object);
        }
        return map;
    }

    @Override
    public <C extends DomainCollection<?>> C createCollection(Class<C> cls, Conditions conditions) {
        return new ProxySchrodingerConditionsDomainCollection<C>(cls, context, this, conditions).create(cls);
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
    public <T extends DomainObject, C extends DomainCollection<T>> Map<String, C> createCollection(Class<T> dCls, Class<C> cCls, Collection<String> limitedIds, Map<String, ? extends Collection<String>> idsMapping) {
        C collection = createCollection(cCls, limitedIds);
        Map<String, C> map = new LinkedHashMap<>();
        for (Map.Entry<String, ? extends Collection<String>> entry : idsMapping.entrySet()) {
            List<T> list = new ArrayList<>();
            for (String id : entry.getValue()) {
                T object = createObject(dCls, collection, id);
                list.add(object);
            }
            C wrap = wrapCollection(cCls, list);
            map.put(entry.getKey(), wrap);
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
}
