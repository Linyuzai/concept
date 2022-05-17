package com.github.linyuzai.connection.loadbalance.core.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SingleThreadScheduledExecutorServiceFactory implements ScheduledExecutorServiceFactory {

    @Override
    public ScheduledExecutorService create(Object key) {
        return Executors.newSingleThreadScheduledExecutor();
    }
}