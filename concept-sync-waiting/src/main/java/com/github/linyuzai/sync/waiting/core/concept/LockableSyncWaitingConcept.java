package com.github.linyuzai.sync.waiting.core.concept;

import com.github.linyuzai.sync.waiting.core.caller.SyncCaller;
import com.github.linyuzai.sync.waiting.core.configuration.SyncWaitingConfiguration;
import lombok.NonNull;
import lombok.SneakyThrows;

public interface LockableSyncWaitingConcept extends SyncWaitingConcept {

    @SneakyThrows
    @Override
    default <T> T waitSync(@NonNull Object key, @NonNull SyncCaller caller, @NonNull SyncWaitingConfiguration configuration) {
        lock();
        try {
            SyncWaiter exist = findWaitingSyncWaiter(key);
            if (exist == null) {
                SyncWaiter waiter = reuseSyncWaiter(key);
                if (waiter == null) {
                    throw new NullPointerException("No SyncWaiter to reuse");
                }
                try {
                    caller.call(key);
                } catch (Throwable e) {
                    recycleSyncWaiter(key);
                    throw e;
                }
                try {
                    waiter.performWait(configuration.getWaitingTime());
                    T value = waiter.value();
                    recycleSyncWaiter(key);
                    return value;
                } catch (Throwable e) {
                    recycleSyncWaiter(key);
                    waiter.performNotify();
                    throw e;
                }
            } else {
                exist.performWait(configuration.getQueuingTime());
                return waitSync(key, caller, configuration);
            }
        } finally {
            unlock();
        }
    }

    @Override
    default void notifyAsync(@NonNull Object key, Object value) {
        lock();
        try {
            SyncWaiter waiter = findWaitingSyncWaiter(key);
            if (waiter != null) {
                waiter.value(value);
                waiter.performNotify();
            }
        } finally {
            unlock();
        }
    }

    @Override
    default boolean isWaiting(@NonNull Object key) {
        lock();
        try {
            return isSyncWaiterWaiting(key);
        } finally {
            unlock();
        }
    }

    void lock();

    void unlock();

    boolean isSyncWaiterWaiting(Object key);

    SyncWaiter findWaitingSyncWaiter(Object key);

    SyncWaiter reuseSyncWaiter(Object key);

    void recycleSyncWaiter(Object key);
}
