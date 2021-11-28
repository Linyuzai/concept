package com.github.linyuzai.sync.waiting.core.concept;

import com.github.linyuzai.sync.waiting.core.container.SyncWaiterContainer;
import com.github.linyuzai.sync.waiting.core.exception.SyncWaitingTimeoutException;
import com.github.linyuzai.sync.waiting.core.recycler.SyncWaiterRecycler;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionSyncWaitingConcept extends AbstractSyncWaitingConcept {

    protected final Lock lock = initLock();

    public ConditionSyncWaitingConcept() {
        super();
    }

    public ConditionSyncWaitingConcept(SyncWaiterContainer container) {
        super(container);
    }

    public ConditionSyncWaitingConcept(SyncWaiterRecycler recycler) {
        super(recycler);
    }

    public ConditionSyncWaitingConcept(SyncWaiterContainer container, SyncWaiterRecycler recycler) {
        super(container, recycler);
    }

    public Lock initLock() {
        return new ReentrantLock(true);
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
        return new ConditionSyncWaiter(lock.newCondition());
    }

    public static class ConditionSyncWaiter extends AbstractSyncWaiter {

        private final Condition condition;

        public ConditionSyncWaiter(@NonNull Condition condition) {
            this.condition = condition;
        }

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
            condition.signalAll();
        }
    }
}
