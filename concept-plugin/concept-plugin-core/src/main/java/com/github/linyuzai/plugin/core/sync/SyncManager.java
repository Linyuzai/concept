package com.github.linyuzai.plugin.core.sync;

import java.util.function.Supplier;

public interface SyncManager {

    void syncRead(Runnable runnable);

    <T> T syncRead(Supplier<T> supplier);

    void syncWrite(Runnable runnable);

    <T> T syncWrite(Supplier<T> supplier);
}
