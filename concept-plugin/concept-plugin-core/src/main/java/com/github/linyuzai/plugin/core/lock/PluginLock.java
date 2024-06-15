package com.github.linyuzai.plugin.core.lock;

public interface PluginLock {

    String LOADING = "LOADING";

    String UNLOADING = "UNLOADING";

    void lock(Object o, Object arg);

    void unlock(Object o, Object arg);

    Object getLockArg(Object o);
}
