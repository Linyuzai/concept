package com.github.linyuzai.sync.waiting.core.recycler;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.Queue;

@AllArgsConstructor
public class QueueSyncWaiterRecycler implements SyncWaiterRecycler {

    @Getter
    @NonNull
    protected Queue<SyncWaiter> queue;

    public QueueSyncWaiterRecycler() {
        this(new LinkedList<>());
    }

    @Override
    public void recycle(SyncWaiter waiter) {
        queue.offer(waiter);
    }

    @Override
    public SyncWaiter reuse() {
        return queue.poll();
    }
}
