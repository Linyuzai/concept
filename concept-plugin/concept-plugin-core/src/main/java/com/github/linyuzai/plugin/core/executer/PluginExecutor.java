package com.github.linyuzai.plugin.core.executer;

import java.util.concurrent.TimeUnit;

/**
 * 插件执行器
 */
public interface PluginExecutor {

    void execute(Runnable runnable);

    void execute(Runnable runnable, long delay, TimeUnit unit);
}
