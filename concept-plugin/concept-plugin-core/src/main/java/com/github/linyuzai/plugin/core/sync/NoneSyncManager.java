package com.github.linyuzai.plugin.core.sync;

import java.util.function.Supplier;

/**
 * 无同步
 */
public class NoneSyncManager implements SyncManager {

    @Override
    public void syncRead(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <T> T syncRead(Supplier<T> supplier) {
        return supplier.get();
    }

    @Override
    public void syncWrite(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <T> T syncWrite(Supplier<T> supplier) {
        return supplier.get();
    }

    public static class Factory implements SyncManagerFactory {

        @Override
        public SyncManager create(Object o) {
            return new NoneSyncManager();
        }
    }
}
