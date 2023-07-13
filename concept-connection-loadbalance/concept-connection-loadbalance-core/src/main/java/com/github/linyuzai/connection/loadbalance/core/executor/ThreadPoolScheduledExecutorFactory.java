package com.github.linyuzai.connection.loadbalance.core.executor;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 定时任务执行器工厂实现。
 * 通过 {@link Executors#newScheduledThreadPool(int)} 实例化线程池，默认线程数为 1。
 * <p>
 * Factory of scheduled executor impl by {@link Executors#newScheduledThreadPool(int)}.
 * Default core size is 1.
 */
@Getter
@Setter
public class ThreadPoolScheduledExecutorFactory extends AbstractScopedFactory<ScheduledExecutor>
        implements ScheduledExecutorFactory {

    private int threadPoolSize;

    @Override
    public ScheduledExecutor create(String scope) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(threadPoolSize);
        return new ThreadPoolScheduledExecutor(service);
    }
}
