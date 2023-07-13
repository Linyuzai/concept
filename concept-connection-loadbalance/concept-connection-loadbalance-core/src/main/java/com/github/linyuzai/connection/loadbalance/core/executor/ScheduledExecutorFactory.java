package com.github.linyuzai.connection.loadbalance.core.executor;

import com.github.linyuzai.connection.loadbalance.core.scope.ScopedFactory;

/**
 * 定时任务执行器工厂。
 * <p>
 * Factory of scheduled executor.
 */
public interface ScheduledExecutorFactory extends ScopedFactory<ScheduledExecutor> {
}
