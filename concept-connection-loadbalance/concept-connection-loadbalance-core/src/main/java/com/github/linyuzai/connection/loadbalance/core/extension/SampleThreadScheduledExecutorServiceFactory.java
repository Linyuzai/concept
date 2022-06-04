package com.github.linyuzai.connection.loadbalance.core.extension;

import lombok.AllArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 默认线程池工厂
 */
@AllArgsConstructor
public class SampleThreadScheduledExecutorServiceFactory implements ScheduledExecutorServiceFactory {

    private final ScheduledExecutorService service;

    /**
     * 复用 {@link Executors#newSingleThreadScheduledExecutor()} 创建的 {@link ScheduledExecutorService}
     */
    public SampleThreadScheduledExecutorServiceFactory() {
        this(Executors.newSingleThreadScheduledExecutor());
    }

    @Override
    public ScheduledExecutorService create(Object key) {
        return service;
    }
}
