package com.github.linyuzai.plugin.core.lock;

public interface PluginLock {

    String LOADING = "LOADING";

    String UNLOADING = "UNLOADING";

    void lock(Object lockable, Object arg);

    void unlock(Object lockable, Object arg);

    Object getLockArg(Object lockable);
}
