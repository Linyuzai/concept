package com.github.linyuzai.sync.waiting.core.recycler;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

/**
 * 基于 {@link Queue} 实现的 {@link SyncWaiterRecycler}。
 */
@AllArgsConstructor
public class QueueSyncWaiterRecycler implements SyncWaiterRecycler {

    /**
     * 队列
     */
    @Getter
    @NonNull
    protected Queue<SyncWaiter> queue;

    /**
     * 无限制的队列。
     */
    public QueueSyncWaiterRecycler() {
        this(new LinkedList<>());
    }

    /**
     * 固定回收数量的队列。
     *
     * @param n 固定的数量
     */
    public QueueSyncWaiterRecycler(int n) {
        this(new Limited<>(n));
    }

    @Override
    public void recycle(SyncWaiter waiter) {
        queue.offer(waiter);
    }

    @Override
    public SyncWaiter reuse() {
        return queue.poll();
    }

    public static class Limited<E> extends AbstractQueue<E> {

        private final Queue<E> queue = new LinkedList<>();

        private final int limit;

        public Limited(int limit) {
            this.limit = limit;
        }

        @Override
        public Iterator<E> iterator() {
            return queue.iterator();
        }

        @Override
        public int size() {
            return queue.size();
        }

        /**
         * 如果到达上限就不再添加到队列中。
         *
         * @param e 元素
         * @return 是否添加成功
         */
        @Override
        public boolean offer(E e) {
            if (size() < limit) {
                return queue.offer(e);
            }
            return false;
        }

        @Override
        public E poll() {
            return queue.poll();
        }

        @Override
        public E peek() {
            return queue.peek();
        }
    }
}
