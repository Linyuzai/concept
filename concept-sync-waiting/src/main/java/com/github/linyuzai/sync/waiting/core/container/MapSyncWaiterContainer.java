package com.github.linyuzai.sync.waiting.core.container;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MapSyncWaiterContainer implements SyncWaiterContainer {

    @Getter
    @NonNull
    protected Map<Object, SyncWaiter> map;

    public MapSyncWaiterContainer() {
        this(new HashMap<>());
    }

    @Override
    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    @Override
    public SyncWaiter find(Object key) {
        return map.get(key);
    }

    @Override
    public void add(SyncWaiter waiter) {
        map.put(waiter.key(), waiter);
    }

    @Override
    public SyncWaiter remove(Object key) {
        return map.remove(key);
    }
}
