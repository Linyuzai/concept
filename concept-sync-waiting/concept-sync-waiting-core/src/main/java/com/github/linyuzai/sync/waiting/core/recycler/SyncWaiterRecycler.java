package com.github.linyuzai.sync.waiting.core.recycler;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;

/**
 * {@link SyncWaiter} 回收器。
 */
public interface SyncWaiterRecycler {

    /**
     * 回收一个 {@link SyncWaiter}。
     *
     * @param waiter 被回收的 {@link SyncWaiter}
     */
    void recycle(SyncWaiter waiter);

    /**
     * 重新使用一个 {@link SyncWaiter}，
     * 如果不存在可以重新使用的则返回 null。
     *
     * @return 重新使用的 {@link SyncWaiter} 或 null
     */
    SyncWaiter reuse();
}
