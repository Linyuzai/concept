package com.github.linyuzai.connection.loadbalance.core.utils;

import java.util.concurrent.ScheduledExecutorService;

public interface ScheduledExecutorServiceFactory {

    ScheduledExecutorService create(Object key);
}
