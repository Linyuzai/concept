package com.github.linyuzai.sync.waiting.core.container;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;

public interface SyncWaiterContainer {

    boolean contains(Object key);

    SyncWaiter find(Object key);

    void add(SyncWaiter waiter);

    SyncWaiter remove(Object key);
}
