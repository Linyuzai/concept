package com.github.linyuzai.plugin.core.executer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
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

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2, this);

    @Override
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public void execute(Runnable runnable, long delay, TimeUnit unit) {
        executor.schedule(runnable, delay, unit);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(group, runnable, "concept-plugin-" + count.incrementAndGet());
    }
}
