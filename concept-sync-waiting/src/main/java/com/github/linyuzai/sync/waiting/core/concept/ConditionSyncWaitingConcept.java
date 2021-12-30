package com.github.linyuzai.sync.waiting.core.concept;

import com.github.linyuzai.sync.waiting.core.container.MapSyncWaiterContainer;
import com.github.linyuzai.sync.waiting.core.container.SyncWaiterContainer;
import com.github.linyuzai.sync.waiting.core.exception.SyncWaitingTimeoutException;
import com.github.linyuzai.sync.waiting.core.recycler.DisposableSyncWaiterRecycler;
import com.github.linyuzai.sync.waiting.core.recycler.SyncWaiterRecycler;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionSyncWaitingConcept extends AbstractSyncWaitingConcept {

    protected final Lock lock;

    protected final boolean signalAll;

    public ConditionSyncWaitingConcept() {
        this(new MapSyncWaiterContainer(), new DisposableSyncWaiterRecycler(), new ReentrantLock(), false);
    }

    protected ConditionSyncWaitingConcept(SyncWaiterContainer container, SyncWaiterRecycler recycler,
                                          @NonNull Lock lock, boolean signalAll) {
        super(container, recycler);
        this.lock = lock;
        this.signalAll = signalAll;
    }

    @SneakyThrows
    @Override
    public void lock() {
        lock.lockInterruptibly();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    protected SyncWaiter createSyncWaiter() {
        return new ConditionSyncWaiter(lock.newCondition(), signalAll);
    }

    @AllArgsConstructor
    public static class ConditionSyncWaiter extends AbstractSyncWaiter {

        @NonNull
        private final Condition condition;

        private final boolean signalAll;

        @SneakyThrows
        @Override
        public void performWait(long time) {
            if (time > 0) {
                if (!condition.await(time, TimeUnit.MILLISECONDS)) {
                    throw new SyncWaitingTimeoutException(time + "ms");
                }
            } else {
                condition.await();
            }
        }

        @Override
        public void performNotify() {
            if (signalAll) {
                condition.signalAll();
            } else {
                condition.signal();
            }
        }
    }

    public static class Builder {

        private Lock lock;

        private boolean fairLock;

        private boolean signalAll;

        private SyncWaiterContainer container;

        private SyncWaiterRecycler recycler;

        public Builder lock(Lock lock) {
            this.lock = lock;
            return this;
        }

        public Builder fairLock(boolean fairLock) {
            this.fairLock = fairLock;
            return this;
        }

        public Builder signalAll(boolean signalAll) {
            this.signalAll = signalAll;
            return this;
        }

        public Builder container(SyncWaiterContainer container) {
            this.container = container;
            return this;
        }

        public Builder recycler(SyncWaiterRecycler recycler) {
            this.recycler = recycler;
            return this;
        }

        public ConditionSyncWaitingConcept build() {
            if (lock == null) {
                lock = new ReentrantLock(fairLock);
            }
            if (container == null) {
                container = new MapSyncWaiterContainer();
            }
            if (recycler == null) {
                recycler = new DisposableSyncWaiterRecycler();
            }
            return new ConditionSyncWaitingConcept(container, recycler, lock, signalAll);
        }
    }
}
