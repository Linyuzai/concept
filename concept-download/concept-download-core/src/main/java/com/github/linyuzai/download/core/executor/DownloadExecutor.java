package com.github.linyuzai.download.core.executor;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public interface DownloadExecutor {

    Executor getExecutor();

    static Executor getExecutor(DownloadContext context) {
        Executor executor = context.get(Executor.class);
        if (executor != null) {
            return executor;
        }
        ExecutorService executorService = context.get(ExecutorService.class);
        if (executorService != null) {
            return executorService;
        }
        ScheduledExecutorService scheduledExecutorService = context.get(ScheduledExecutorService.class);
        if (scheduledExecutorService != null) {
            return scheduledExecutorService;
        }
        return null;
    }
}
