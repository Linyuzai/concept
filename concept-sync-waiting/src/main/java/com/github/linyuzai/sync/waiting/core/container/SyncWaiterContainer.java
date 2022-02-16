package com.github.linyuzai.sync.waiting.core.container;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;

/**
 * 存放等待中的 {@link SyncWaiter} 的容器。
 */
public interface SyncWaiterContainer {

    /**
     * 判断是否存在对应 key 的等待中的 {@link SyncWaiter}。
     *
     * @param key 标识
     * @return 如果存在则返回 true
     */
    boolean contains(Object key);

    /**
     * 根据 key 查找 {@link SyncWaiter}，不存在则返回 null。
     *
     * @param key 标识
     * @return 找到的 {@link SyncWaiter} 或 null
     */
    SyncWaiter find(Object key);

    /**
     * 添加一个等待中的 {@link SyncWaiter}。
     *
     * @param waiter 添加的 {@link SyncWaiter}
     */
    void add(SyncWaiter waiter);

    /**
     * 移除一个等待中的 {@link SyncWaiter}。
     *
     * @param key 标识
     * @return 被移除的 {@link SyncWaiter}
     */
    SyncWaiter remove(Object key);
}
