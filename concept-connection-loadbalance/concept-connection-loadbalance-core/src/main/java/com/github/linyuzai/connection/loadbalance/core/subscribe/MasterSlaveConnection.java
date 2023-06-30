package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public interface MasterSlaveConnection extends Connection {

    Connection getMaster();

    Connection getSlave();

    Connection getCurrent();

    long getSwitchTimestamp();

    void switchMaster();

    void switchSlave();

    boolean isMaster(Connection connection);

    boolean isSlave(Connection connection);

    void lock();

    void unlock();
}
