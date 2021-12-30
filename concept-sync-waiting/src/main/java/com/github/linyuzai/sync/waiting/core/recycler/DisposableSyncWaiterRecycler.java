package com.github.linyuzai.sync.waiting.core.recycler;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;

public class DisposableSyncWaiterRecycler implements SyncWaiterRecycler {

    @Override
    public void recycle(SyncWaiter waiter) {

    }

    @Override
    public SyncWaiter reuse() {
        return null;
    }
}
