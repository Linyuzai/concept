package com.github.linyuzai.connection.loadbalance.core.extension;

import java.util.concurrent.ScheduledExecutorService;

public interface ScheduledExecutorServiceFactory {

    ScheduledExecutorService create(Object key);
}
