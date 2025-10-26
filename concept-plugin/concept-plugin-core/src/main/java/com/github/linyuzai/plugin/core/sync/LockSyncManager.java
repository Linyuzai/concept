package com.github.linyuzai.plugin.core.sync;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * 基于 {@link ReadWriteLock} 的同步管理器
 */
public class LockSyncManager implements SyncManager {

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void syncRead(Runnable runnable) {
        lock.readLock().lock();
        try {
            runnable.run();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public <T> T syncRead(Supplier<T> supplier) {
        lock.readLock().lock();
        try {
            return supplier.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void syncWrite(Runnable runnable) {
        lock.writeLock().lock();
        try {
            runnable.run();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public <T> T syncWrite(Supplier<T> supplier) {
        lock.writeLock().lock();
        try {
            return supplier.get();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static class Factory implements SyncManagerFactory {

        @Override
        public SyncManager create(Object o) {
            return new LockSyncManager();
        }
    }
}
