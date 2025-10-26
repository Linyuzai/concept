package com.github.linyuzai.plugin.core.executer;

import java.util.concurrent.TimeUnit;

/**
 * 插件执行器
 */
public interface PluginExecutor {

    /**
     * 执行
     */
    void execute(Runnable runnable);

    /**
     * 延迟执行
     */
    void execute(Runnable runnable, long delay, TimeUnit unit);

    /**
     * 定时执行
     */
    void schedule(Runnable runnable, long period, TimeUnit unit);

    /**
     * 关闭
     */
    void shutdown();
}
