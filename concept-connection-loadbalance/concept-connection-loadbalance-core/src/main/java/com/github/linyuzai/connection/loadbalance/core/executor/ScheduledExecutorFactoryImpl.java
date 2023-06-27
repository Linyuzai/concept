package com.github.linyuzai.connection.loadbalance.core.executor;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@Setter
public class ScheduledExecutorFactoryImpl extends AbstractScopedFactory<ScheduledExecutor>
        implements ScheduledExecutorFactory {

    private int size = 1;

    @Override
    public ScheduledExecutor create(String scope) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(size);
        return new ScheduledExecutorImpl(service);
    }
}
