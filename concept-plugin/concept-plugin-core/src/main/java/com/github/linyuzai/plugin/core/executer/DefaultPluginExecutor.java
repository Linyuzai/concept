package com.github.linyuzai.plugin.core.executer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 默认的插件执行器
 */
public class DefaultPluginExecutor extends AbstractPluginExecutor {

    @Override
    protected ScheduledExecutorService newScheduledExecutorService(ThreadFactory threadFactory) {
        return Executors.newScheduledThreadPool(2, threadFactory);
    }
}
