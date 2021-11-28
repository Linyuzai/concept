package com.github.linyuzai.sync.waiting.core.concept;

import com.github.linyuzai.sync.waiting.core.container.MapSyncWaiterContainer;
import com.github.linyuzai.sync.waiting.core.container.SyncWaiterContainer;
import com.github.linyuzai.sync.waiting.core.recycler.QueueSyncWaiterRecycler;
import com.github.linyuzai.sync.waiting.core.recycler.SyncWaiterRecycler;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public abstract class AbstractSyncWaitingConcept implements LockableSyncWaitingConcept {

    @NonNull
    protected final SyncWaiterContainer container;

    @NonNull
    protected final SyncWaiterRecycler recycler;

    public AbstractSyncWaitingConcept() {
        this(new MapSyncWaiterContainer(), new QueueSyncWaiterRecycler());
    }

    public AbstractSyncWaitingConcept(SyncWaiterContainer container) {
        this(container, new QueueSyncWaiterRecycler());
    }

    public AbstractSyncWaitingConcept(SyncWaiterRecycler recycler) {
        this(new MapSyncWaiterContainer(), recycler);
    }

    @Override
    public boolean isSyncWaiterWaiting(Object key) {
        return container.contains(key);
    }

    @Override
    public SyncWaiter findWaitingSyncWaiter(Object key) {
        return container.find(key);
    }

    @Override
    public SyncWaiter reuseSyncWaiter(Object key) {
        SyncWaiter waiter = reuseOrCreate();
        if (waiter == null) {
            throw new NullPointerException("No SyncWaiter reuse or create");
        }
        waiter.key(key);
        container.add(waiter);
        return waiter;
    }

    protected SyncWaiter reuseOrCreate() {
        SyncWaiter waiter = recycler.reuse();
        if (waiter == null) {
            return createSyncWaiter();
        } else {
            return waiter;
        }
    }

    protected abstract SyncWaiter createSyncWaiter();

    @Override
    public void recycleSyncWaiter(Object key) {
        resetAndRecycle(container.remove(key));
    }

    protected void resetAndRecycle(SyncWaiter waiter) {
        if (waiter == null) {
            return;
        }
        waiter.key(null);
        waiter.value(null);
        recycler.recycle(waiter);
    }

    public static abstract class AbstractSyncWaiter implements SyncWaiter {

        private Object key;

        private Object value;

        @Override
        public Object key() {
            return key;
        }

        @Override
        public void key(Object key) {
            this.key = key;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T value() {
            return (T) value;
        }

        @Override
        public void value(Object value) {
            this.value = value;
        }
    }
}
