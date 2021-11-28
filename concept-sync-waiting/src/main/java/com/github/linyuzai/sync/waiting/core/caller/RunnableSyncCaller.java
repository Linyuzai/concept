package com.github.linyuzai.sync.waiting.core.caller;

public interface RunnableSyncCaller extends SyncCaller, Runnable {

    @Override
    default void call(Object key) {
        run();
    }
}
