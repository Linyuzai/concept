package com.github.linyuzai.connection.loadbalance.core.extension;

import java.util.concurrent.ScheduledExecutorService;

/**
 * {@link ScheduledExecutorService} 工厂
 */
public interface ScheduledExecutorServiceFactory {

    ScheduledExecutorService create(Object key);
}
