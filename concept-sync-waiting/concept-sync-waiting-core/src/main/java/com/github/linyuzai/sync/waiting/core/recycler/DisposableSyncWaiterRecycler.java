package com.github.linyuzai.sync.waiting.core.recycler;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;

/**
 * 不进行回收的 {@link SyncWaiterRecycler}。
 */
public class DisposableSyncWaiterRecycler implements SyncWaiterRecycler {

    @Override
    public void recycle(SyncWaiter waiter) {

    }

    @Override
    public SyncWaiter reuse() {
        return null;
    }
}
