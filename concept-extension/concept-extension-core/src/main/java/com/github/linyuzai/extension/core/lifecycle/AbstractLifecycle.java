package com.github.linyuzai.extension.core.lifecycle;

public abstract class AbstractLifecycle implements Lifecycle {

    private final Object lock = new Object();

    private volatile boolean initialized = false;

    @Override
    public void initialize() {
        if (!initialized) {
            synchronized (lock) {
                if (!initialized) {
                    onInitialize();
                    initialized = true;
                    onInitialized();
                }
            }
        }
    }

    @Override
    public boolean initialized() {
        return initialized;
    }

    @Override
    public void destroy() {
        if (initialized) {
            synchronized (lock) {
                if (initialized) {
                    onDestroy();
                    initialized = false;
                    onDestroyed();
                }
            }
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
