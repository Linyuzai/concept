package com.github.linyuzai.connection.loadbalance.autoconfigure.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TagScope implements Scope {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @NonNull
    @Override
    public Object get(@NonNull String name, @NonNull ObjectFactory<?> objectFactory) {
        return cache.computeIfAbsent(name, key -> objectFactory.getObject());
    }

    @Override
    public Object remove(@NonNull String name) {
        return cache.remove(name);
    }

    @Override
    public void registerDestructionCallback(@NonNull String name, @NonNull Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(@NonNull String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
