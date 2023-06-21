package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;
import lombok.AllArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 默认线程池工厂
 */
@AllArgsConstructor
public class SingleThreadScheduledExecutorServiceFactory extends AbstractScopedFactory<ScheduledExecutorService>
        implements ScheduledExecutorServiceFactory {

    private final ScheduledExecutorService service;

    /**
     * 复用 {@link Executors#newSingleThreadScheduledExecutor()} 创建的 {@link ScheduledExecutorService}
     */
    public SingleThreadScheduledExecutorServiceFactory() {
        this(Executors.newSingleThreadScheduledExecutor());
    }

    @Override
    public ScheduledExecutorService create(String scope) {
        return service;
    }
}
