package com.github.linyuzai.extension.core.lifecycle;

public abstract class AbstractLifecycle implements Lifecycle {

    private volatile boolean initialized = false;

    @Override
    public synchronized void initialize() {
        if (!initialized) {
            onInitialize();
            initialized = true;
            onInitialized();
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
            onDestroyed();
        }
    }

    public void onInitialize() {

    }

    public void onInitialized() {

    }

    public void onDestroy() {

    }

    public void onDestroyed() {

    }

    /*private enum Initialization {
        UNINITIALIZED, INITIALIZING, INITIALIZED
    }*/
}
