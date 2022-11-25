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

/**
 * 基于 {@link Condition } 实现的 {@link SyncWaitingConcept}。
 */
public class ConditionSyncWaitingConcept extends AbstractSyncWaitingConcept {

    /**
     * 锁
     */
    protected final Lock lock;

    public ConditionSyncWaitingConcept() {
        this(new MapSyncWaiterContainer(), new DisposableSyncWaiterRecycler(), new ReentrantLock());
    }

    protected ConditionSyncWaitingConcept(SyncWaiterContainer container,
                                          SyncWaiterRecycler recycler,
                                          @NonNull Lock lock) {
        super(container, recycler);
        this.lock = lock;
    }

    /**
     * 加锁。
     */
    @SneakyThrows
    @Override
    public void lock() {
        lock.lockInterruptibly();
    }

    /**
     * 解锁。
     */
    @Override
    public void unlock() {
        lock.unlock();
    }

    /**
     * 创建一个 {@link ConditionSyncWaiter}。
     *
     * @return 新建的 {@link ConditionSyncWaiter}
     */
    @Override
    public SyncWaiter createSyncWaiter() {
        return new ConditionSyncWaiter(lock.newCondition());
    }

    /**
     * 基于 {@link Condition } 实现的 {@link SyncWaiter}。
     */
    @AllArgsConstructor
    public static class ConditionSyncWaiter extends AbstractSyncWaiter {

        @NonNull
        private final Condition condition;

        /**
         * 如果超时时间大于 0 则调用 {@link Condition#await(long, TimeUnit)}，
         * 否则调用 {@link Condition#await()}。
         *
         * @param time 等待超时时间
         */
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

        /**
         * 调用 {@link Condition#signalAll()}。
         */
        @Override
        public void performNotify() {
            condition.signalAll();
        }
    }

    public static class Builder {

        private Lock lock;

        private SyncWaiterContainer container;

        private SyncWaiterRecycler recycler;

        public Builder lock(Lock lock) {
            this.lock = lock;
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
                lock = new ReentrantLock();
            }
            if (container == null) {
                container = new MapSyncWaiterContainer();
            }
            if (recycler == null) {
                recycler = new DisposableSyncWaiterRecycler();
            }
            return new ConditionSyncWaitingConcept(container, recycler, lock);
        }
    }
}
