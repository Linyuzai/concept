package com.github.linyuzai.connection.loadbalance.core.extension;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 复用 {@link Executors#newSingleThreadScheduledExecutor()} 创建的 {@link ScheduledExecutorService}
 */
public class SingleThreadScheduledExecutorServiceFactory implements ScheduledExecutorServiceFactory {

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    @Override
    public ScheduledExecutorService create(Object key) {
        return service;
    }
}
