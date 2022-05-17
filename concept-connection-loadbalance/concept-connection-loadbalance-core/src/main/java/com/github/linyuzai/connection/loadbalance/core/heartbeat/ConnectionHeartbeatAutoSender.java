package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

public class ConnectionHeartbeatAutoSender extends ScheduledExecutorConnectionHeartbeatAutoSupport {

    public ConnectionHeartbeatAutoSender(String connectionType,
                                         long timeout, long period,
                                         ScheduledExecutorService executor) {
        super(Collections.singletonList(connectionType), timeout, period, executor);
    }

    public ConnectionHeartbeatAutoSender(Collection<String> connectionTypes,
                                         long timeout, long period,
                                         ScheduledExecutorService executor) {
        super(connectionTypes, timeout, period, executor);
    }

    @Override
    public void schedule() {
        closeTimeout();
        sendPing();
    }
}
