package com.github.linyuzai.download.core.executor;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadLifecycleListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@RequiredArgsConstructor
public class DownloadExecutorInitializer implements DownloadLifecycleListener {

    private final DownloadExecutor downloadExecutor;

    @Override
    public void onStart(DownloadContext context) {
        context.set(DownloadExecutor.class, downloadExecutor);
        Executor executor = downloadExecutor.getExecutor();
        context.set(Executor.class, executor);
        if (executor instanceof ExecutorService) {
            context.set(ExecutorService.class, executor);
        }
        if (executor instanceof ScheduledExecutorService) {
            context.set(ScheduledExecutorService.class, executor);
        }
    }
}
