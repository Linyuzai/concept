package com.github.linyuzai.plugin.core.sync;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class TimeoutSyncManager implements SyncManager {

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected final long timeout;

    @SneakyThrows
    @Override
    public void syncRead(Runnable runnable) {
        boolean tryLock = lock.readLock().tryLock(timeout, TimeUnit.MILLISECONDS);
        if (!tryLock) {
            throw newTimeoutException();
        }
        try {
            runnable.run();
        } finally {
            lock.readLock().unlock();
        }
    }

    @SneakyThrows
    @Override
    public <T> T syncRead(Supplier<T> supplier) {
        boolean tryLock = lock.readLock().tryLock(timeout, TimeUnit.MILLISECONDS);
        if (!tryLock) {
            throw newTimeoutException();
        }
        try {
            return supplier.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    @SneakyThrows
    @Override
    public void syncWrite(Runnable runnable) {
        boolean tryLock = lock.writeLock().tryLock(timeout, TimeUnit.MILLISECONDS);
        if (!tryLock) {
            throw newTimeoutException();
        }
        try {
            runnable.run();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SneakyThrows
    @Override
    public <T> T syncWrite(Supplier<T> supplier) {
        boolean tryLock = lock.writeLock().tryLock(timeout, TimeUnit.MILLISECONDS);
        if (!tryLock) {
            throw newTimeoutException();
        }
        try {
            return supplier.get();
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected TimeoutException newTimeoutException() {
        return new TimeoutException("Sync timeout");
    }

    public static class Factory implements SyncManagerFactory {

        @Override
        public SyncManager create(Object o) {
            return new TimeoutSyncManager(30_000);
        }
    }
}
