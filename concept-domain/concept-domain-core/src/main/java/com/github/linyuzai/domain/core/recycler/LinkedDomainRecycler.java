package com.github.linyuzai.domain.core.recycler;

import com.github.linyuzai.domain.core.DomainObject;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class LinkedDomainRecycler implements DomainRecycler {

    private final Map<Object, Map<Class<? extends DomainObject>, Queue<DomainObject>>> pool
            = new ConcurrentHashMap<>();

    @Override
    public <T extends DomainObject> boolean recycle(Object recycleType, Class<T> domainType, T recyclable) {
        recyclable.release();
        return queue(recycleType, domainType).offer(recyclable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DomainObject> T reuse(Object recycleType, Class<T> domainType, Supplier<T> supplier) {
        DomainObject polled = queue(recycleType, domainType).poll();
        if (polled == null) {
            return supplier.get();
        } else {
            return (T) polled;
        }
    }

    protected Queue<DomainObject> queue(Object recycleType, Class<? extends DomainObject> domainType) {
        return pool.computeIfAbsent(recycleType, rt -> new ConcurrentHashMap<>())
                .computeIfAbsent(domainType, dt -> new ConcurrentLinkedQueue<>());
    }
}
