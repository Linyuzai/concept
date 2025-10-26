package com.github.linyuzai.plugin.core.executer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 插件执行器抽象类
 */
public abstract class AbstractPluginExecutor implements PluginExecutor, ThreadFactory {

    private final AtomicInteger count = new AtomicInteger(0);

    private final ThreadGroup group = new ThreadGroup("concept-plugin");

    private final ScheduledExecutorService executor = newScheduledExecutorService(this);

    @Override
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public void execute(Runnable runnable, long delay, TimeUnit unit) {
        executor.schedule(runnable, delay, unit);
    }

    @Override
    public void schedule(Runnable runnable, long period, TimeUnit unit) {
        executor.scheduleWithFixedDelay(runnable, 0, period, unit);
    }

    @Override
    public void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(group, runnable, "concept-plugin-" + count.incrementAndGet());
    }

    protected abstract ScheduledExecutorService newScheduledExecutorService(ThreadFactory threadFactory);
}
