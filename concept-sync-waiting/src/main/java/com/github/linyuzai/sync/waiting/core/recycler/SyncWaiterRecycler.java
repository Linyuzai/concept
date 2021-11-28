package com.github.linyuzai.sync.waiting.core.recycler;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;

public interface SyncWaiterRecycler {

    void recycle(SyncWaiter waiter);

    SyncWaiter reuse();
}
