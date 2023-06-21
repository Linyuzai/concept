package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.scope.ScopedFactory;

import java.util.concurrent.ScheduledExecutorService;

/**
 * {@link ScheduledExecutorService} 工厂
 */
public interface ScheduledExecutorServiceFactory extends ScopedFactory<ScheduledExecutorService> {

}
