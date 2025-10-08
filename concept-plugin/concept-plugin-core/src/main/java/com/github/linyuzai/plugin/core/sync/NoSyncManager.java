package com.github.linyuzai.plugin.core.sync;

import java.util.function.Supplier;

public class NoSyncManager implements SyncManager {

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
}
