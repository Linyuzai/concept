package com.github.linyuzai.sync.waiting.core.concept;

import com.github.linyuzai.sync.waiting.core.caller.SyncCaller;
import com.github.linyuzai.sync.waiting.core.configuration.SyncWaitingConfiguration;
import com.github.linyuzai.sync.waiting.core.container.SyncWaiterContainer;
import com.github.linyuzai.sync.waiting.core.recycler.SyncWaiterRecycler;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * {@link SyncWaitingConcept} 的抽象类。
 */
@AllArgsConstructor
public abstract class AbstractSyncWaitingConcept implements SyncWaitingConcept {

    @NonNull
    protected final SyncWaiterContainer container;

    @NonNull
    protected final SyncWaiterRecycler recycler;

    /**
     * 阻塞等待。
     * 每个请求都会加锁，
     * 通过 key 获得当前等待的 {@link SyncWaiter}，
     * 如果已经存在对应的 {@link SyncWaiter}，
     * 则当前线程排队等待不进行业务逻辑调用，
     * 直到上一个对应 key 的请求线程被唤醒或超时后再重试；
     * 如果不存在对应的 {@link SyncWaiter}，
     * 重新使用一个 {@link SyncWaiter}，
     * 调用业务接口 {@link SyncCaller}，
     * 如果调用过程中发生异常，
     * 回收 {@link SyncCaller} 并抛出异常，
     * 如果调用成功执行阻塞等待，
     * 成功唤醒则回收 {@link SyncCaller} 并返回获得的值，
     * 否则回收 {@link SyncCaller} 并直接唤醒，同时抛出异常，
     * 最后解锁。
     *
     * @param key           标识
     * @param caller        业务调用回调
     * @param configuration 等待配置 {@link SyncWaitingConfiguration}
     * @param <T>           值类型
     * @return 值
     */
    @Override
    public <T> T waitSync(@NonNull Object key, @NonNull SyncCaller caller, @NonNull SyncWaitingConfiguration configuration) {
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

    /**
     * 异步唤醒。
     * 加锁，
     * 获得正在等待中的 {@link SyncWaiter}，
     * 如果不为 null，
     * 则设置值并唤醒对应线程，
     * 解锁。
     *
     * @param key   标识
     * @param value 值
     */
    @Override
    public void notifyAsync(@NonNull Object key, Object value) {
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

    /**
     * 是否等待。
     * 加锁，
     * 返会是否存在等待中的 {@link SyncWaiter}，
     * 解锁。
     *
     * @param key 标识
     * @return 如果存在等待中的 {@link SyncWaiter} 则返回 true
     */
    @Override
    public boolean isWaiting(@NonNull Object key) {
        lock();
        try {
            return isSyncWaiterWaiting(key);
        } finally {
            unlock();
        }
    }

    /**
     * 是否存在等待中的 {@link SyncWaiter}。
     *
     * @param key 标识
     * @return 如果存在等待中的 {@link SyncWaiter} 则返回 true
     */
    public boolean isSyncWaiterWaiting(Object key) {
        return container.contains(key);
    }

    /**
     * 根据 key 获得正在等待中的 {@link SyncWaiter}，
     * 没有则返回 null。
     *
     * @param key 标识
     * @return 正在等待中的 {@link SyncWaiter} 或 null
     */
    public SyncWaiter findWaitingSyncWaiter(Object key) {
        return container.find(key);
    }

    /**
     * 重新使用一个 {@link SyncWaiter}。
     *
     * @param key 标识
     * @return 重新使用的 {@link SyncWaiter}
     */
    public SyncWaiter reuseSyncWaiter(Object key) {
        SyncWaiter waiter = reuseOrCreate();
        if (waiter == null) {
            throw new NullPointerException("No SyncWaiter reuse or create");
        }
        waiter.key(key);
        container.add(waiter);
        return waiter;
    }

    /**
     * 重新使用或创建一个 {@link SyncWaiter}。
     *
     * @return 重新使用或创建的 {@link SyncWaiter}
     */
    public SyncWaiter reuseOrCreate() {
        SyncWaiter waiter = recycler.reuse();
        if (waiter == null) {
            return createSyncWaiter();
        } else {
            return waiter;
        }
    }

    /**
     * 加锁。
     */
    public abstract void lock();

    /**
     * 解锁。
     */
    public abstract void unlock();

    /**
     * 创建一个 {@link SyncWaiter}。
     *
     * @return 新建的 {@link SyncWaiter}
     */
    public abstract SyncWaiter createSyncWaiter();

    /**
     * 回收 {@link SyncWaiter}。
     *
     * @param key 标识
     */
    public void recycleSyncWaiter(Object key) {
        resetAndRecycle(container.remove(key));
    }

    /**
     * 重置并回收 {@link SyncWaiter}。
     *
     * @param waiter 需要重置和回收的 {@link SyncWaiter}。
     */
    public void resetAndRecycle(SyncWaiter waiter) {
        if (waiter == null) {
            return;
        }
        waiter.key(null);
        waiter.value(null);
        recycler.recycle(waiter);
    }

    /**
     * {@link SyncWaiter} 的抽象类。
     */
    public static abstract class AbstractSyncWaiter implements SyncWaiter {

        /**
         * 标识
         */
        private Object key;

        /**
         * 值
         */
        private Object value;

        /**
         * 获得标识。
         *
         * @return 标识
         */
        @Override
        public Object key() {
            return key;
        }

        /**
         * 设置标识。
         *
         * @param key 标识
         */
        @Override
        public void key(Object key) {
            this.key = key;
        }

        /**
         * 获得值。
         *
         * @param <T> 值类型
         * @return 值
         */
        @SuppressWarnings("unchecked")
        @Override
        public <T> T value() {
            return (T) value;
        }

        /**
         * 设置值。
         *
         * @param value 值
         */
        @Override
        public void value(Object value) {
            this.value = value;
        }
    }
}
