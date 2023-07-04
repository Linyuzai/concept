package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;

public interface MasterSlaveConnectionSubscriber extends ConnectionSubscriber {

    MasterSlave getMasterSlave();

    void setMasterSlave(MasterSlave masterSlave);
}
