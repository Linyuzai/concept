package com.github.linyuzai.download.core.load;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 基于线程池的调用器 / Invoker based on thread pool executor
 */
@AllArgsConstructor
public class ExecutorSourceLoaderInvoker extends ConcurrentSourceLoaderInvoker {

    @Getter
    @Setter
    private Executor executor;

    /**
     * 使用线程池执行 / Execute by thread pool executor
     *
     * @param runnable {@link Runnable}
     */
    @Override
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * 关闭线程池 / Shutdown the thread pool executor
     */
    public void shutdown() {
        if (executor instanceof ExecutorService) {
            ExecutorService executorService = (ExecutorService) executor;
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }
}
