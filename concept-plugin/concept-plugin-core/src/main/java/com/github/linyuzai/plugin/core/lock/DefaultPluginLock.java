package com.github.linyuzai.plugin.core.lock;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultPluginLock implements PluginLock {

    private final Map<Object, LockObject> lockMap = new ConcurrentHashMap<>();

    @Override
    public void lock(Object o, Object arg) {
        LockObject lock = lockMap.computeIfAbsent(o, key -> new LockObject());
        if (lock.tryLock()) {
            lock.arg = arg;
            return;
        }
        throw new PluginLockException(o, lock.arg, arg);
    }

    @Override
    public void unlock(Object o, Object arg) {
        LockObject lock = lockMap.remove(o);
        if (lock == null) {
            return;
        }
        lock.unlock();
    }

    @Override
    public Object getLockArg(Object o) {
        System.out.println(lockMap);
        LockObject lock = lockMap.get(o);
        if (lock == null) {
            return null;
        }
        return lock.arg;
    }

    @Getter
    public static class LockObject extends ReentrantLock {

        private volatile Object arg;

        @Override
        public String toString() {
            return String.valueOf(arg);
        }
    }
}
