package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import java.util.concurrent.ScheduledExecutorService;

public class ConnectionHeartbeatAutoSender extends ScheduledExecutorConnectionHeartbeatAutoSupport {

    public ConnectionHeartbeatAutoSender(String connectionType,
                                         long timeout, long period,
                                         ScheduledExecutorService executor) {
        super(connectionType, timeout, period, executor);
    }

    @Override
    public void schedule() {
        closeTimeout();
        sendPing();
    }
}
