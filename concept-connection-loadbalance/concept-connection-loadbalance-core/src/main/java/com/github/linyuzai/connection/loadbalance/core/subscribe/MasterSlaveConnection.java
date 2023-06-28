package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.util.concurrent.locks.Lock;

public interface MasterSlaveConnection extends Connection {

    Lock getLock();

    Connection getCurrent();

    boolean isMaster(Connection connection);

    boolean isSlave(Connection connection);

    void switchMaster();

    void switchSlave(Object key);

    long getSwitchTimestamp();
}
