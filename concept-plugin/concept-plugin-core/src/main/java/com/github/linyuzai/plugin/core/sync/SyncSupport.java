package com.github.linyuzai.plugin.core.sync;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * 同步支持类
 */
@Getter
@Setter
public abstract class SyncSupport {

    @Getter
    @Setter
    private static SyncManagerFactory syncManagerFactory = new LockSyncManager.Factory();

    private SyncManager syncManager = syncManagerFactory.create(this);

    protected void syncRead(Runnable runnable) {
        syncManager.syncRead(runnable);
    }

    protected <T> T syncRead(Supplier<T> supplier) {
        return syncManager.syncRead(supplier);
    }

    protected void syncWrite(Runnable runnable) {
        syncManager.syncWrite(runnable);
    }

    protected <T> T syncWrite(Supplier<T> supplier) {
        return syncManager.syncWrite(supplier);
    }
}
