package com.github.linyuzai.plugin.core.executer;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认的插件执行器
 * <p>
 * 默认2个线程
 * <p>
 * 一个用于监听插件文件
 * <p>
 * 另一个用于可视化页面异步操作
 */
public class DefaultPluginExecutor implements PluginExecutor, ThreadFactory {

    private final AtomicInteger count = new AtomicInteger(0);

    private final ThreadGroup group = new ThreadGroup("concept-plugin");

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4, this);

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
    public Executor asExecutor() {
        return executor;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(group, runnable, "concept-plugin-" + count.incrementAndGet());
    }
}
