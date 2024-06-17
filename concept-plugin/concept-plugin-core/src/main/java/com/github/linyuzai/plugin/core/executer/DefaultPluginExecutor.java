package com.github.linyuzai.plugin.core.executer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultPluginExecutor implements PluginExecutor, ThreadFactory {

    private final AtomicInteger count = new AtomicInteger(0);

    private final ThreadGroup group = new ThreadGroup("concept-plugin");

    private final Executor executor = Executors.newFixedThreadPool(2, this);

    @Override
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(group, r, "concept-plugin-" + count.incrementAndGet());
    }
}
