package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public interface MasterSlaveConnection extends Connection {

    Connection getCurrent();

    void switchMaster();

    void switchSlave(Object key);
}
