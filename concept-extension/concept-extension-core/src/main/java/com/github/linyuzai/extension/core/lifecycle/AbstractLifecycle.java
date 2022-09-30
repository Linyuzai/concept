package com.github.linyuzai.extension.core.lifecycle;

public abstract class AbstractLifecycle implements Lifecycle {

    private volatile boolean initialized = false;

    @Override
    public synchronized void initialize() {
        if (!initialized) {
            onInitialize();
            initialized = true;
        }
    }

    @Override
    public synchronized boolean initialized() {
        return initialized;
    }

    @Override
    public synchronized void destroy() {
        if (initialized) {
            onDestroy();
            initialized = false;
        }
    }

    public abstract void onInitialize();

    public abstract void onDestroy();

    /*private enum Initialization {
        UNINITIALIZED, INITIALIZING, INITIALIZED
    }*/
}
