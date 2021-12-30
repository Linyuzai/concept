package com.github.linyuzai.sync.waiting.core.recycler;

import com.github.linyuzai.sync.waiting.core.concept.SyncWaiter;
import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueSyncWaiterRecycler extends QueueSyncWaiterRecycler {

    private final int maxCount;

    private int currentCount = 0;

    public BlockingQueueSyncWaiterRecycler() {
        this(10);
    }

    public BlockingQueueSyncWaiterRecycler(int maxCount) {
        this(new LinkedBlockingQueue<>(), maxCount);
    }

    public BlockingQueueSyncWaiterRecycler(BlockingQueue<SyncWaiter> queue, int maxCount) {
        super(queue);
        this.maxCount = maxCount;
    }

    @SneakyThrows
    @Override
    public SyncWaiter reuse() {
        if (needTake()) {
            return ((BlockingQueue<SyncWaiter>) queue).take();
        } else {
            return null;
        }
    }

    protected boolean needTake() {
        if (queue.size() > 0) {
            return true;
        } else {
            if (currentCount < maxCount) {
                currentCount++;
                return false;
            } else {
                return true;
            }
        }
    }
}
