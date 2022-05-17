package com.github.linyuzai.connection.loadbalance.core.subscribe.monitor;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.utils.ConnectionLoadBalanceLogger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SubscribeMonitorLogger extends ConnectionLoadBalanceLogger implements ConnectionEventListener {

    public SubscribeMonitorLogger(Consumer<String> info, BiConsumer<String, Throwable> error) {
        super(info, error);
    }

    @Override
    public void onEvent(Object event) {
        if (event instanceof SubscribeMonitorEvent) {
            info("Start running subscribe monitor");
        }
    }
}
