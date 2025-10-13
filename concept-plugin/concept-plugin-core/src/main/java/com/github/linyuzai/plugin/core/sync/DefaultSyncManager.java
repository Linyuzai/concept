package com.github.linyuzai.plugin.core.sync;

import java.util.function.Supplier;

public class DefaultSyncManager implements SyncManager {

    @Override
    public void syncRead(Runnable runnable) {
        synchronized (this) {
            runnable.run();
        }
    }

    @Override
    public <T> T syncRead(Supplier<T> supplier) {
        synchronized (this) {
            return supplier.get();
        }
    }

    @Override
    public void syncWrite(Runnable runnable) {
        synchronized (this) {
            runnable.run();
        }
    }

    @Override
    public <T> T syncWrite(Supplier<T> supplier) {
        synchronized (this) {
            return supplier.get();
        }
    }

    public static class Factory implements SyncManagerFactory {

        @Override
        public SyncManager create(Object o) {
            return new DefaultSyncManager();
        }
    }
}
